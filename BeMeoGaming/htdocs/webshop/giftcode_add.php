<?php
require_once '../core/set.php';
require_once '../core/connect.php';
require_once '../core/head.php';
$pdo = new PDO("mysql:host=localhost;dbname=dragonballsaga;charset=utf8", "root", "");

// Kiểm tra đăng nhập
if ($_login === null) {
    echo '<script>window.location.href = "../dang-nhap.php";</script>';
    exit;
}

// Chỉ cho phép tài khoản admin
if ($_admin != 1) {
    echo '<script>window.location.href="/"</script>';
    exit;
}

// ================== AJAX SAVE (không reload) ==================
if (isset($_GET['ajax']) && $_GET['ajax'] == '1' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    header('Content-Type: application/json; charset=utf-8');

    try {
        // Validate cơ bản
        $code       = trim($_POST['code'] ?? '');
        $count_left = (int)($_POST['count_left'] ?? 0);
        $expired    = trim($_POST['expired'] ?? '');

        if ($code === '' || $count_left < 0 || $expired === '') {
            echo json_encode(['ok'=>false, 'msg'=>'Vui lòng nhập đủ thông tin giftcode.']); exit;
        }

        // Dữ liệu item/option (chọn hoặc nhập tay)
        $itemIdArr        = $_POST['item_id']         ?? [];
        $itemIdManualArr  = $_POST['item_id_manual']  ?? [];
        $quantityArr      = $_POST['quantity']        ?? [];
        $optIdArr         = $_POST['option_id']       ?? []; // option_id[i][] theo index item
        $optIdManualArr   = $_POST['option_id_manual']?? []; // option_id_manual[i][]
        $optParamArr      = $_POST['option_param']    ?? []; // option_param[i][]

        $items = [];
        $cnt = max(count($itemIdArr), count($itemIdManualArr), count($quantityArr));
        for ($i = 0; $i < $cnt; $i++) {
            // Ưu tiên ID nhập tay
            $temp_id = 0;
            if (!empty($itemIdManualArr[$i])) {
                $temp_id = (int)$itemIdManualArr[$i];
            } elseif (isset($itemIdArr[$i]) && $itemIdArr[$i] !== '') {
                $temp_id = (int)$itemIdArr[$i];
            }
            $quantity = isset($quantityArr[$i]) ? (int)$quantityArr[$i] : 0;

            if ($temp_id < 0 || $quantity <= 0) {
                continue; // bỏ item không hợp lệ
            }

            // Options cho item i
            $options = [];
            $optIds      = $optIdArr[$i]        ?? [];
            $optIdsMan   = $optIdManualArr[$i]  ?? [];
            $optParams   = $optParamArr[$i]     ?? [];

            $optCount = max(count($optIds), count($optIdsMan), count($optParams));
            for ($k = 0; $k < $optCount; $k++) {
                // Ưu tiên option id nhập tay
                $oid = 0;
                if (!empty($optIdsMan[$k])) {
                    $oid = (int)$optIdsMan[$k];
                } elseif (isset($optIds[$k]) && $optIds[$k] !== '') {
                    $oid = (int)$optIds[$k];
                }
                $param = isset($optParams[$k]) ? (int)$optParams[$k] : 0;

                if ($oid > 0) {
                    $options[] = [
                        'id'    => (string)$oid,
                        'param' => (string)$param,
                    ];
                }
            }

            $items[] = [
                'temp_id'  => (string)$temp_id,
                'quantity' => (string)$quantity,
                'options'  => $options,
            ];
        }

        if (empty($items)) {
            echo json_encode(['ok'=>false, 'msg'=>'Chưa có item hợp lệ để lưu.']); exit;
        }

        // Lưu DB: bảng giftcode (code, count_left, detail, datecreate, expired)
        $detail = json_encode($items, JSON_UNESCAPED_UNICODE);
        $stmt = $pdo->prepare("INSERT INTO giftcode (code, count_left, detail, datecreate, expired) VALUES (?, ?, ?, NOW(), ?)");
        $stmt->execute([$code, $count_left, $detail, $expired]);

        echo json_encode(['ok'=>true, 'msg'=>'Đã lưu giftcode thành công!', 'items_saved'=>count($items)]);
    } catch (Throwable $e) {
        echo json_encode(['ok'=>false, 'msg'=>'Lỗi: '.$e->getMessage()]);
    }
    exit;
}

// ================== LOAD DATA DROPDOWNS ==================
$itemsQuery   = $pdo->query("SELECT id, name FROM item_template ORDER BY name ASC");
$optionsQuery = $pdo->query("SELECT id, name FROM item_option_template ORDER BY name ASC");
$itemsList    = $itemsQuery->fetchAll(PDO::FETCH_ASSOC);
$optionsList  = $optionsQuery->fetchAll(PDO::FETCH_ASSOC);
?>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Thêm Giftcode</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<style>
    body { background-color: #f8f9fa; padding: 20px; }
    .form-container { background-color: #fff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
    .item-block, .option-block { padding: 15px; margin-bottom: 15px; border: 1px solid #e9ecef; border-radius: 10px; background-color: #f8fafc; }
    .item-header { display: flex; align-items: center; justify-content: space-between; }
    pre { background: #0f172a; color: #e2e8f0; padding: 15px; border-radius: 10px; max-height: 340px; overflow: auto; }
    .muted { color: #6c757d; font-size: 0.9rem; }
</style>
</head>
<body>
<div class="container">
    <h2 class="mb-4 text-center">Thêm Giftcode</h2>
    <div id="flash"></div>

    <div class="form-container">
        <form id="giftcodeForm" onsubmit="return false;">
            <div class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Mã Giftcode</label>
                    <input type="text" class="form-control" name="code" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Số lượt sử dụng</label>
                    <input type="number" class="form-control" name="count_left" min="0" value="1" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Ngày hết hạn</label>
                    <input type="date" class="form-control" name="expired" required>
                </div>
            </div>

            <hr class="my-4">
            <div class="d-flex align-items-center justify-content-between">
                <h5 class="mb-2">Danh sách Item</h5>
                <div>
                    <button type="button" class="btn btn-primary" id="btnAddItem">+ Thêm Item</button>
                    <button type="button" class="btn btn-outline-secondary" id="btnResetItems">Xoá hết items</button>
                </div>
            </div>

            <div id="itemsContainer" class="mt-3"></div>

            <div class="mb-3">
                <h6 class="mb-2">Preview JSON</h6>
                <pre id="jsonPreview">[]</pre>
                <div class="muted">JSON này chính là cấu trúc sẽ lưu vào cột <code>detail</code>.</div>
            </div>

            <div class="d-flex gap-2">
                <button type="button" class="btn btn-success" id="btnSave">Lưu Giftcode (AJAX)</button>
                <button type="button" class="btn btn-outline-danger" id="btnClearForm">Xoá form</button>
            </div>
        </form>
    </div>
</div>

<script>
// ====== Data từ PHP ======
const ITEMS = <?php echo json_encode($itemsList, JSON_UNESCAPED_UNICODE); ?>;
const OPTS  = <?php echo json_encode($optionsList, JSON_UNESCAPED_UNICODE); ?>;

let itemCount = 0;

// Build <option> helper
function buildOptions(rows, placeholder='-- Chọn --') {
    let h = `<option value="">${placeholder}</option>`;
    rows.forEach(r => {
        const id = Number(r.id);
        const name = (r.name ?? '').toString().replace(/</g,'&lt;').replace(/>/g,'&gt;');
        h += `<option value="${id}">${name} (ID: ${id})</option>`;
    });
    return h;
}

const ITEM_SELECT_HTML = buildOptions(ITEMS, '-- Chọn Item --');
const OPT_SELECT_HTML  = buildOptions(OPTS,  '-- Chọn Option --');

// ====== UI: add/remove item/option ======
$('#btnAddItem').on('click', addItem);
$('#btnResetItems').on('click', () => { $('#itemsContainer').empty(); updatePreview(); });
$('#btnClearForm').on('click', () => {
    $('#giftcodeForm')[0].reset();
    $('#itemsContainer').empty();
    updatePreview();
});

function addItem() {
    itemCount++;
    const idx = itemCount; // 1-based để map option group theo item

    const card = $(`
        <div class="item-block" data-item-index="${idx}">
            <div class="item-header mb-2">
                <h6 class="mb-0">Item #${idx}</h6>
                <div>
                    <button type="button" class="btn btn-sm btn-outline-secondary me-1" data-action="addOption">+ Option</button>
                    <button type="button" class="btn btn-sm btn-outline-danger" data-action="removeItem">Xoá Item</button>
                </div>
            </div>

            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Chọn Item</label>
                    <select class="form-select" name="item_id[]">${ITEM_SELECT_HTML}</select>
                    <div class="form-text">Hoặc nhập ID thủ công:</div>
                    <input type="number" class="form-control" name="item_id_manual[]" placeholder="Nhập Item ID">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Số lượng</label>
                    <input type="number" class="form-control" name="quantity[]" min="1" value="1">
                </div>
            </div>

            <div class="mt-3">
                <div class="optionsContainer"></div>
                <button type="button" class="btn btn-sm btn-outline-primary mt-2" data-action="addOption">+ Thêm Option</button>
                <div class="form-text">Không bắt buộc có option.</div>
            </div>
        </div>
    `);

    // Events trong card
    card.on('click', '[data-action="removeItem"]', function(){
        card.remove();
        updatePreview();
    });
    card.on('click', '[data-action="addOption"]', function(){
        addOption(card);
    });

    // input change => cập nhật preview
    card.on('input change', 'select, input', updatePreview);

    $('#itemsContainer').append(card);
    updatePreview();
}

function addOption($itemCard) {
    const $optsWrap = $itemCard.find('.optionsContainer');
    const optionRow = $(`
        <div class="option-block">
            <div class="row g-3 align-items-end">
                <div class="col-md-6">
                    <label class="form-label">Chọn Option</label>
                    <select class="form-select option-id">${OPT_SELECT_HTML}</select>
                    <div class="form-text">Hoặc nhập ID thủ công:</div>
                    <input type="number" class="form-control option-id-manual" placeholder="Nhập Option ID">
                </div>
                <div class="col-md-4">
                    <label class="form-label">Param</label>
                    <input type="number" class="form-control option-param" value="0">
                </div>
                <div class="col-md-2">
                    <button type="button" class="btn btn-outline-danger w-100" data-action="removeOption">Xoá</button>
                </div>
            </div>
        </div>
    `);
    optionRow.on('click', '[data-action="removeOption"]', function(){
        optionRow.remove();
        updatePreview();
    });
    optionRow.on('input change', 'select, input', updatePreview);

    $optsWrap.append(optionRow);
    updatePreview();
}

// ====== JSON Preview: đọc trực tiếp DOM (chắc chắn đúng nhóm) ======
function readItemsFromDOM(){
    const items = [];
    $('#itemsContainer .item-block').each(function(){
        const $card = $(this);
        const selId = Number($card.find('select[name="item_id[]"]').val() || 0);
        const manId = Number($card.find('input[name="item_id_manual[]"]').val() || 0);
        const temp_id = manId > 0 ? manId : selId;

        const quantity = Number($card.find('input[name="quantity[]"]').val() || 0);

        if (temp_id >= 0 && quantity > 0) {
            const options = [];
            $card.find('.option-block').each(function(){
                const $row = $(this);
                const selOpt = Number($row.find('.option-id').val() || 0);
                const manOpt = Number($row.find('.option-id-manual').val() || 0);
                const oid = manOpt > 0 ? manOpt : selOpt;
                const prm = Number($row.find('.option-param').val() || 0);
                if (oid > 0) {
                    options.push({ id: String(oid), param: String(prm) });
                }
            });

            items.push({
                temp_id: String(temp_id),
                quantity: String(quantity),
                options
            });
        }
    });
    return items;
}

function updatePreview(){
    const items = readItemsFromDOM();
    $('#jsonPreview').text(JSON.stringify(items, null, 2));
}

$('#giftcodeForm').on('input change', 'input, select', updatePreview);

// ====== SAVE AJAX (không reload, không mất form) ======
$('#btnSave').on('click', async function(){
    const items = readItemsFromDOM();
    if (items.length < 0) {
    flash(false, 'Chưa có item hợp lệ để lưu.');
    return;
}

    // Kiểm tra trường giftcode
    const code = $('input[name="code"]').val().trim();
    const count_left = Number($('input[name="count_left"]').val() || 0);
    const expired = $('input[name="expired"]').val().trim();
    if (!code || !expired || count_left < 0) {
        flash(false, 'Vui lòng nhập đủ thông tin giftcode.');
        return;
    }

    // Build FormData theo đúng cấu trúc PHP đang đọc
    const fd = new FormData(document.getElementById('giftcodeForm'));

    // Gắn lại option theo nhóm item (đảm bảo PHP nhận đúng index)
    // Lưu ý: phần inputs/ selects của option không nằm trong name[] gửi sẵn,
    // ta đóng gói lại vào cấu trúc option_id[i][], option_id_manual[i][], option_param[i][]
    // Xoá các option cũ nếu có
    // (Tránh trùng lặp: ở đây ta chỉ bổ sung các field option_* để đúng index)
    $('#itemsContainer .item-block').each(function(i){
        const $card = $(this);
        // Item
        // (form đã có item_id[] / item_id_manual[] / quantity[] sẵn)

        // Options
        const optIdName       = `option_id[${i}][]`;
        const optIdManName    = `option_id_manual[${i}][]`;
        const optParamName    = `option_param[${i}][]`;

        $card.find('.option-block').each(function(){
            const $row = $(this);
            const selOpt = $row.find('.option-id').val() || '';
            const manOpt = $row.find('.option-id-manual').val() || '';
            const prm    = $row.find('.option-param').val() || '0';

            fd.append(optIdName, selOpt);
            fd.append(optIdManName, manOpt);
            fd.append(optParamName, prm);
        });
    });

    try {
        const res = await fetch(window.location.pathname + '?ajax=1', {
            method: 'POST',
            body: fd
        });
        const data = await res.json();
        if (data.ok) {
            flash(true, `${data.msg} (Items: ${data.items_saved})`);
            // Không reset form để không mất dữ liệu đang nhập
            // Nếu muốn reset, bật 2 dòng dưới:
            // $('#giftcodeForm')[0].reset();
            // $('#itemsContainer').empty();
        } else {
            flash(false, data.msg || 'Có lỗi xảy ra.');
        }
    } catch (e) {
        console.error(e);
        flash(false, 'Lỗi mạng hoặc máy chủ.');
    }
});

function flash(ok, msg){
    const html = `<div class="alert ${ok?'alert-success':'alert-danger'}">${msg}</div>`;
    $('#flash').html(html);
    setTimeout(()=> $('#flash').empty(), 4500);
}
</script>
</body>
</html>
<?php include_once '../core/footer.php'; ?>
