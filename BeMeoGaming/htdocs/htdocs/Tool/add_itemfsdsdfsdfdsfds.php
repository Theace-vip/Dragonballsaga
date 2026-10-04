<?php
// ---------- DB CONNECT ----------
$dsn = "mysql:host=localhost;dbname=hondaodragon;charset=utf8";
$user = "root";
$pass = "";
$options = [
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
];
$pdo = new PDO($dsn, $user, $pass, $options);

// ---------- LOAD DATA FOR DROPDOWNS ----------
$tabs = $pdo->query("SELECT id, shop_id, tab_name, tab_index FROM tab_shop ORDER BY shop_id, tab_index, id")->fetchAll();
$typeSellList = $pdo->query("SELECT id, name FROM type_sell_item_shop ORDER BY id")->fetchAll();
$itemTemplates = $pdo->query("SELECT id, name FROM item_template ORDER BY name")->fetchAll();
$itemOptions = $pdo->query("SELECT id, name FROM item_option_template ORDER BY name")->fetchAll();

// helper build <option>
function buildOptionsHTML($rows, $valueKey='id', $labelKey='name') {
    $html = '<option value="">-- chọn --</option>';
    foreach ($rows as $r) {
        $v = (int)$r[$valueKey];
        $label = htmlspecialchars($r[$labelKey] ?? (string)$v, ENT_QUOTES, 'UTF-8');
        $html .= "<option value=\"{$v}\">{$label} (ID: {$v})</option>";
    }
    return $html;
}
$typeSellOptionsHTML      = buildOptionsHTML($typeSellList);
$itemTemplateOptionsHTML  = buildOptionsHTML($itemTemplates);
$itemOptionOptionsHTML    = buildOptionsHTML($itemOptions);

// ---------- AJAX SUBMIT (no reload) ----------
if (isset($_GET['ajax']) && $_GET['ajax'] == '1' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    header('Content-Type: application/json; charset=utf-8');

    try {
        $tab_id = isset($_POST['tab_id']) ? (int)$_POST['tab_id'] : 0;
        if ($tab_id <= 0) {
            echo json_encode(['ok'=>false,'msg'=>'Vui lòng chọn Tab Shop hợp lệ.']); exit;
        }

        // Dữ liệu items từ form
        $costArr        = $_POST['cost']              ?? [];
        $typeSellArr    = $_POST['type_sell']         ?? [];
        $tempIdArr      = $_POST['temp_id']           ?? [];
        $tempManualArr  = $_POST['temp_id_manual']    ?? [];
        $itemSpecArr    = $_POST['item_spec']         ?? [];
        $isNewArr       = $_POST['is_new']            ?? []; // checkbox indexed by i
        $isSellArr      = $_POST['is_sell']           ?? []; // checkbox indexed by i
        $optIdArr       = $_POST['option_id']         ?? []; // option_id[i][k]
        $optParamArr    = $_POST['option_param']      ?? []; // option_param[i][k]
        $optIdManualArr = $_POST['option_id_manual'] ?? [];

        $countItems = max(count($costArr), count($typeSellArr), count($tempIdArr), count($itemSpecArr));
        $newItems = [];

        for ($i = 0; $i < $countItems; $i++) {
            // ưu tiên manual nếu có
            $temp_id = 0;
            if (!empty($tempManualArr[$i])) {
                $temp_id = (int)$tempManualArr[$i];
            } elseif (isset($tempIdArr[$i]) && $tempIdArr[$i] !== '') {
                $temp_id = (int)$tempIdArr[$i];
            }

            $cost       = isset($costArr[$i]) ? (int)$costArr[$i] : null;
            $type_sell  = isset($typeSellArr[$i]) ? (int)$typeSellArr[$i] : null;
            $item_spec  = isset($itemSpecArr[$i]) ? (int)$itemSpecArr[$i] : null;

            if ($temp_id < 0 || $type_sell === null || $cost === null || $item_spec === null) {
                continue; // bỏ item không hợp lệ
            }

            $optionsList = [];
            if (!empty($optIdArr[$i])) {
    foreach ($optIdArr[$i] as $k => $oid) {
        $manual = $optIdManualArr[$i][$k] ?? '';
        $finalId = $manual !== '' ? (int)$manual : (int)$oid;
        if ($finalId <= 0 || !isset($optParamArr[$i][$k])) continue;
        $optionsList[] = [
            "param" => (int)$optParamArr[$i][$k],
            "id"    => $finalId,
        ];
    }
}


            $newItems[] = [
                "cost"      => $cost,
                "type_sell" => $type_sell,
                "is_new"    => isset($isNewArr[$i]),
                "temp_id"   => $temp_id,
                "item_spec" => $item_spec,
                "options"   => $optionsList,
                "is_sell"   => isset($isSellArr[$i]),
            ];
        }

        if (empty($newItems)) {
            echo json_encode(['ok'=>false,'msg'=>'Chưa có sản phẩm hợp lệ để thêm.']); exit;
        }

        // Lấy items hiện tại trong tab
        $stmt = $pdo->prepare("SELECT items FROM tab_shop WHERE id = ?");
        $stmt->execute([$tab_id]);
        $row = $stmt->fetch();

        $existingItems = [];
        if ($row && !empty($row['items'])) {
            $decoded = json_decode($row['items'], true);
            if (is_array($decoded)) $existingItems = $decoded;
        }

        // append
        $merged = array_values(array_merge($existingItems, $newItems));
        $jsonItems = json_encode($merged, JSON_UNESCAPED_UNICODE);

        // update
        $up = $pdo->prepare("UPDATE tab_shop SET items = ? WHERE id = ?");
        $up->execute([$jsonItems, $tab_id]);

        echo json_encode([
            'ok' => true,
            'msg' => 'Đã thêm sản phẩm vào Tab thành công!',
            'added' => count($newItems),
            'total' => count($merged)
        ]);
    } catch (Throwable $e) {
        echo json_encode(['ok'=>false,'msg'=>'Lỗi: '.$e->getMessage()]);
    }
    exit;
}
?>
<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<title>Thêm sản phẩm vào Tab Shop (AJAX, no reload)</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
    .item-card{border:1px solid #e5e7eb;border-radius:12px;padding:16px;margin-bottom:16px;background:#f8fafc}
    .option-row{margin-bottom:8px}
    pre.jsonbox{background:#0f172a;color:#e2e8f0;padding:12px;border-radius:8px;max-height:300px;overflow:auto}
</style>
</head>
<body class="container py-4">
    <h2 class="mb-4">Thêm sản phẩm vào Tab Shop</h2>

    <div id="flash"></div>

    <form id="shopForm" onsubmit="return false;">
        <!-- Select Tab Shop -->
        <div class="row g-3 mb-3">
            <div class="col-md-6">
                <label class="form-label">Chọn Tab Shop</label>
                <select id="tabSelect" class="form-select" required>
                    <option value="">-- Chọn Tab --</option>
                    <?php foreach ($tabs as $t): ?>
                        <option value="<?= (int)$t['id'] ?>"
                            data-shop="<?= (int)$t['shop_id'] ?>"
                            data-name="<?= htmlspecialchars($t['tab_name'], ENT_QUOTES, 'UTF-8') ?>"
                            data-index="<?= (int)$t['tab_index'] ?>">
                            Shop <?= (int)$t['shop_id'] ?> — <?= htmlspecialchars($t['tab_name'], ENT_QUOTES, 'UTF-8') ?> (Index: <?= (int)$t['tab_index'] ?>) [Tab ID: <?= (int)$t['id'] ?>]
                        </option>
                    <?php endforeach; ?>
                </select>
                <div class="form-text">Chọn tab muốn thêm sản phẩm.</div>
            </div>
            <div class="col-md-2">
                <label class="form-label">Shop ID</label>
                <input type="number" id="shop_id" class="form-control" readonly>
            </div>
            <div class="col-md-2">
                <label class="form-label">Tab ID</label>
                <input type="number" id="tab_id_show" class="form-control" readonly>
            </div>
            <div class="col-md-2">
                <label class="form-label">Tab Index</label>
                <input type="number" id="tab_index" class="form-control" readonly>
            </div>
            <div class="col-md-6">
                <label class="form-label">Tab Name</label>
                <input type="text" id="tab_name" class="form-control" readonly>
            </div>
        </div>

        <!-- Hidden field for POST -->
        <input type="hidden" name="tab_id" id="tab_id_hidden">

        <hr class="my-4">

        <h4 class="mb-3">Danh sách sản phẩm</h4>
        <div id="items"></div>
        <button type="button" class="btn btn-primary mb-3" onclick="addItem()">+ Thêm sản phẩm</button>

        <h5>JSON preview (chỉ các sản phẩm bạn sắp thêm)</h5>
        <pre id="jsonPreview" class="jsonbox">[]</pre>

        <button type="button" class="btn btn-success" id="btnSave">Lưu vào Tab Shop (AJAX)</button>
    </form>

<script>
// --- Prebuilt options HTML from PHP ---
const TYPE_SELL_OPTIONS = `<?= $typeSellOptionsHTML ?>`;
const ITEM_TPL_OPTIONS  = `<?= $itemTemplateOptionsHTML ?>`;
const ITEM_OPT_OPTIONS  = `<?= $itemOptionOptionsHTML ?>`;

// --- Tab autofill ---
document.getElementById('tabSelect').addEventListener('change', function () {

    const opt = this.options[this.selectedIndex];
    document.getElementById('shop_id').value = opt.getAttribute('data-shop') || '';
    document.getElementById('tab_id_show').value = this.value || '';
    document.getElementById('tab_index').value = opt.getAttribute('data-index') || '';
    document.getElementById('tab_name').value = opt.getAttribute('data-name') || '';
    document.getElementById('tab_id_hidden').value = this.value || '';
        // Sau khi fill thông tin Tab
    loadExistingItems(this.value);
});

async function loadExistingItems(tabId) {
    if (!tabId) {
        document.getElementById('items').innerHTML = '';
        document.getElementById('jsonPreview').textContent = '[]';
        return;
    }
    try {
        const res = await fetch('fetch_items.php?tab_id=' + tabId);
        const data = await res.json();
        if (Array.isArray(data)) {
            renderExistingItems(data);
        } else {
            document.getElementById('items').innerHTML = '';
        }
    } catch (e) {
        console.error('Lỗi load items:', e);
    }
}

function renderExistingItems(items) {
    const wrap = document.getElementById('items');
    wrap.innerHTML = '';
    items.forEach((it, idx) => {
        const card = document.createElement('div');
        card.className = 'item-card';
        card.innerHTML = `
            <h6>Sản phẩm hiện có #${idx+1}</h6>
            <pre class="jsonbox">${JSON.stringify(it, null, 2)}</pre>
        `;
        wrap.appendChild(card);
    });
}

// --- Add item card ---
function addItem() {
    const wrap = document.getElementById('items');
    const idx = document.querySelectorAll('.item-card').length;

    const card = document.createElement('div');
    card.className = 'item-card';
    card.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-2">
            <h6 class="mb-0">Sản phẩm #${idx+1}</h6>
            <button type="button" class="btn btn-sm btn-outline-danger" onclick="this.closest('.item-card').remove(); updateJSONPreview();">Xóa SP</button>
        </div>

        <div class="row g-3">
            <div class="col-md-3">
                <label class="form-label">Cost</label>
                <input type="number" name="cost[]" class="form-control" required>
            </div>
            <div class="col-md-3">
                <label class="form-label">Type Sell</label>
                <select name="type_sell[]" class="form-select" required>
                    ${TYPE_SELL_OPTIONS}
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label">Item (temp_id)</label>
                <select name="temp_id[]" class="form-select">
                    ${ITEM_TPL_OPTIONS}
                </select>
                <input type="number" name="temp_id_manual[]" class="form-control mt-1" placeholder="Hoặc nhập ID">
                <div class="form-text">Nếu nhập ID thủ công, hệ thống sẽ ưu tiên dùng ID này.</div>
            </div>
            <div class="col-md-3">
                <label class="form-label">Item Spec</label>
                <input type="number" name="item_spec[]" class="form-control" required>
            </div>

            <div class="col-md-3 form-check mt-2">
                <input class="form-check-input" type="checkbox" name="is_new[${idx}]" id="is_new_${idx}">
                <label class="form-check-label" for="is_new_${idx}">Mới</label>
            </div>
            <div class="col-md-3 form-check mt-2">
                <input class="form-check-input" type="checkbox" name="is_sell[${idx}]" id="is_sell_${idx}" checked>
                <label class="form-check-label" for="is_sell_${idx}">Cho phép bán</label>
            </div>
        </div>

        <div class="mt-3">
            <div id="options_${idx}"></div>
            <button type="button" class="btn btn-sm btn-outline-primary mt-2" onclick="addOption(${idx})">+ Thêm option</button>
        </div>
    `;
    wrap.appendChild(card);
    updateJSONPreview();
}

// --- Add option row for item idx ---
function addOption(itemIndex) {
    const box = document.getElementById('options_' + itemIndex);
    const row = document.createElement('div');
    row.className = 'option-row row g-2 align-items-end';
    row.innerHTML = `
    <div class="col-md-4">
        <label class="form-label">Option</label>
        <select name="option_id[${itemIndex}][]" class="form-select">
            ${ITEM_OPT_OPTIONS}
        </select>
        <input type="number" name="option_id_manual[${itemIndex}][]" class="form-control mt-1" placeholder="Hoặc nhập ID">
    </div>
    <div class="col-md-3">
        <label class="form-label">Param</label>
        <input type="number" name="option_param[${itemIndex}][]" class="form-control" value="0">
    </div>
    <div class="col-md-2">
        <button type="button" class="btn btn-outline-danger" onclick="this.closest('.option-row').remove(); updateJSONPreview();">Xóa</button>
    </div>
`;

    box.appendChild(row);
    updateJSONPreview();
}

// --- Live JSON preview ---
document.getElementById('shopForm').addEventListener('input', updateJSONPreview);

function updateJSONPreview() {
    const items = [];
    document.querySelectorAll('.item-card').forEach((card, i) => {
        const cost      = valNum(card.querySelector('input[name="cost[]"]'));
        const type_sell = valNum(card.querySelector('select[name="type_sell[]"]'));
        const temp_id_select  = valNum(card.querySelector('select[name="temp_id[]"]'));
        const temp_id_manual  = valNum(card.querySelector('input[name="temp_id_manual[]"]'));
        const temp_id   = temp_id_manual > 0 ? temp_id_manual : temp_id_select;
        const item_spec = valNum(card.querySelector('input[name="item_spec[]"]'));
        const is_new    = card.querySelector(`input[name="is_new[${i}]"]`)?.checked ?? false;
        const is_sell   = card.querySelector(`input[name="is_sell[${i}]"]`)?.checked ?? false;

        const options = [];
        card.querySelectorAll(`#options_${i} .option-row`).forEach(r => {
            const oidSelect = r.querySelector('select[name^="option_id"]').value;
const oidManual = r.querySelector('input[name^="option_id_manual"]').value;
const finalOid = oidManual ? Number(oidManual) : Number(oidSelect);
const prm = r.querySelector('input[name^="option_param"]').value;
if (finalOid) options.push({ param: Number(prm || 0), id: finalOid });

        });

        if (!isNaN(temp_id) && !isNaN(type_sell) && !isNaN(cost) && !isNaN(item_spec)) {
            items.push({
                cost: cost,
                type_sell: type_sell,
                is_new: is_new,
                temp_id: temp_id,
                item_spec: item_spec,
                options: options,
                is_sell: is_sell
            });
        }
    });
    document.getElementById('jsonPreview').textContent = JSON.stringify(items, null, 2);
}

function valNum(el){ return Number(el?.value ?? ''); }

// --- AJAX submit (no reload) ---
document.getElementById('btnSave').addEventListener('click', async function () {
    const tabId = document.getElementById('tab_id_hidden').value;
    if (!tabId) {
        showFlash(false, 'Vui lòng chọn Tab Shop trước khi lưu.');
        return;
    }
    const form = document.getElementById('shopForm');
    const fd = new FormData(form);

    try {
        const res = await fetch(window.location.pathname + '?ajax=1', {
            method: 'POST',
            body: fd
        });
        const data = await res.json();

        if (data.ok) {
    showFlash(true, `${data.msg} (Thêm ${data.added} sp, tổng ${data.total})`);
    // ✅ Không reset Tab, chỉ reset danh sách mới
    document.getElementById('items').innerHTML = '';
    document.getElementById('jsonPreview').textContent = '[]';
    // ✅ Load lại dữ liệu hiện có trong tab
    loadExistingItems(document.getElementById('tab_id_hidden').value);
} else {
            showFlash(false, data.msg || 'Có lỗi xảy ra.');
        }
    } catch (e) {
        showFlash(false, 'Lỗi mạng hoặc máy chủ.');
    }
});

function showFlash(success, message) {
    const box = document.getElementById('flash');
    box.innerHTML = `<div class="alert ${success ? 'alert-success':'alert-danger'}">${message}</div>`;
    setTimeout(()=>{ box.innerHTML=''; }, 4000);
}
</script>
</body>
</html>
