#!/usr/bin/env bash
# sync-all.sh — Dong bo toan bo len GitHub bang 1 lenh.
#   [1/3] nhanch main     : source BeMeoGaming, tools, data, docs
#                            (sua code, them cai trang / hao quang / pet...)
#   [2/3] nhanch web      : folder "Web Nro KOL" (site PHP nap the)
#   [3/3] nhanch database  : dump MySQL (tables/*.sql, day du account + player)
#
# Chay:   ./sync-all.sh    hoac    sync-all.bat
# Khoi phuc DB: xem database/restore.sh tren nhanch database.
set -u

REPO_DIR="$(cd "$(dirname "$0")" && pwd)"
REMOTE="${SYNC_REMOTE:-https://github.com/Theace-vip/Dragonballsaga.git}"
WEB_SRC="${SYNC_WEB_SRC:-/c/Users/Administrator/Downloads/Web Nro KOL}"
MYSQL="${SYNC_MYSQL:-/c/xampp/mysql/bin/mysql.exe}"
MYSQLDUMP="${SYNC_MYSQLDUMP:-/c/xampp/mysql/bin/mysqldump.exe}"
DB_NAME="${SYNC_DB:-hondaodragon}"
MAX_MB=99                     # GitHub chi cho phep file <= 100MB (GH001)
STAMP="$(date '+%F %T')"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
FAIL=0

log()  { echo "[sync] $*"; }
warn() { echo "[sync][CANH BAO] $*"; FAIL=1; }

# ---------------------------------------------------------------- commit+push
# $1 = thu muc repo, $2 = ten nhanh, $3 = thong diep commit
commit_and_push() {
    local dir="$1" ref="$2" msg="$3"
    git -C "$dir" add -A
    # kiem tra SAU add (autocrlf co the lam status "dirty" nhung khong doi noi dung)
    if git -C "$dir" diff --cached --quiet; then
        log "  khong co gi doi — bo qua"
        return 0
    fi
    local n
    n=$(git -C "$dir" diff --cached --name-only | wc -l)
    git -C "$dir" commit -q -m "$msg ($n file thay doi, $STAMP)

🤖 Generated with Codebuff
Co-Authored-By: Codebuff <noreply@codebuff.com>"
    if git -C "$dir" push --quiet origin "HEAD:refs/heads/$ref"; then
        log "  da push nhanch $ref — commit $(git -C "$dir" rev-parse --short HEAD), $n file"
    else
        warn "push nhanch $ref THAT BAI — thu lai bang ./sync-all.sh"
    fi
}

# Clone nhanh mot nhanh tu remote; neu nhanh chua co thi init moi.
# $1 = ten nhanh, $2 = duong dan dich
clone_branch() {
    local ref="$1" dir="$2"
    if git ls-remote --exit-code "$REMOTE" "refs/heads/$ref" >/dev/null 2>&1; then
        git clone --quiet --branch "$ref" --single-branch --depth 5 "$REMOTE" "$dir" \
            || warn "clone nhanh $ref that bai"
    else
        git init --quiet -b main "$dir" && git -C "$dir" remote add origin "$REMOTE"
        log "  nhanh $ref chua co tren remote — se tao moi"
    fi
}

# ============================================================ [1/3] nhanch main
log "== [1/3] nhanch main — source game (BeMeoGaming, tools, data) =="
cd "$REPO_DIR"
if [ -n "$(git status --porcelain)" ]; then
    git add -A
    BIG=""
    while IFS= read -r f; do
        [ -f "$f" ] || continue
        sz=$(stat -c %s "$f" 2>/dev/null || echo 0)
        if [ "$sz" -gt $((MAX_MB * 1024 * 1024)) ]; then
            BIG="$BIG
    $f ($((sz / 1048576))MB)"
        fi
    done < <(git diff --cached --name-only --diff-filter=ACM)
    if [ -n "$BIG" ]; then
        warn "Co file vuot 100MB (GitHub tu choi GH001) — bo qua push main:$BIG
    Go bo / .gitignore file do roi chay lai."
        git reset -q
    else
        commit_and_push "$REPO_DIR" main "sync: cap nhat source game"
    fi
else
    log "  sach — khong co gi doi"
fi

# ============================================================= [2/3] nhanch web
log "== [2/3] nhanch web — Web Nro KOL =="
WEB_DIR="$TMP/web"
if [ ! -d "$WEB_SRC" ]; then
    warn "khong tim thay folder web: $WEB_SRC"
else
    clone_branch web "$WEB_DIR"
    if [ -d "$WEB_DIR/.git" ]; then
        # thay toan bo noi dung (giu .git) — xoa luon file bi xoa trong folder goc
        find "$WEB_DIR" -mindepth 1 -maxdepth 1 ! -name .git -exec rm -rf {} +
        cp -a "$WEB_SRC/." "$WEB_DIR/"
        commit_and_push "$WEB_DIR" web "sync web: cap nhat Web Nro KOL"
    fi
fi

# ======================================================= [3/3] nhanch database
log "== [3/3] nhanch database — MySQL $DB_NAME =="
DB_DIR="$TMP/db"
clone_branch database "$DB_DIR"
if [ -d "$DB_DIR/.git" ]; then
    # file huong dan (chi ghi lan dau, giu nguyen sau do)
    [ -f "$DB_DIR/README.md" ] || cat > "$DB_DIR/README.md" <<'EOF'
# Database backup (nhanh `database`)

Dump toan bo MySQL `hondaodragon` — 1 file cho moi bang, tai `tables/*.sql`.
Moi file co `DROP TABLE IF EXISTS` + `SET FOREIGN_KEY_CHECKS=0` nen
khoi phuc theo bat ky thu tu nao cung duoc.

## Khoi phuc

```bash
./restore.sh                # khoi phuc vao database "hondaodragon"
./restore.sh ten_db_khac    # hoac database khac
```

Can `mysql` trong PATH (Windows XAMPP: `C:\xampp\mysql\bin`).

## Chay lai backup

Chay `sync-all.sh` o goc repo (can MySQL dang chay).
EOF
    [ -f "$DB_DIR/restore.sh" ] || cat > "$DB_DIR/restore.sh" <<'EOF'
#!/usr/bin/env bash
# Khoi phuc database tu tables/*.sql
#   ./restore.sh [ten_database]
# Can mysql trong PATH, hoac dat bien MYSQL=/duong/dan/mysql
set -u
DB="${1:-hondaodragon}"
MYSQL="${MYSQL:-mysql}"
cd "$(dirname "$0")"
"$MYSQL" -h127.0.0.1 -uroot -e "CREATE DATABASE IF NOT EXISTS \`$DB\` DEFAULT CHARACTER SET utf8mb4;" || exit 1
for f in tables/*.sql; do
    [ -f "$f" ] || { echo "Khong tim thay tables/*.sql"; exit 1; }
    echo ">> $f"
    "$MYSQL" -h127.0.0.1 -uroot --default-character-set=utf8mb4 "$DB" < "$f" || exit 1
done
echo "Hoan tat: khoi phuc database [$DB]"
EOF
    chmod +x "$DB_DIR/restore.sh"

    # -B tren Windows tra ket qua CRLF → bo \r, khong thi mysqldump loi "Couldn't find table"
    TABLES=$("$MYSQL" -h127.0.0.1 -uroot -N -B \
        -e "SELECT table_name FROM information_schema.tables
            WHERE table_schema='$DB_NAME' AND table_type='BASE TABLE'
            ORDER BY table_name" 2>"$TMP/mysql.err" | tr -d '\r')
    if [ -z "$TABLES" ]; then
        warn "khong dump duoc DB (MySQL chay khong? loi: $(cat "$TMP/mysql.err"))"
    else
        mkdir -p "$DB_DIR/tables"
        rm -f "$DB_DIR"/tables/*.sql          # xoa file bang da bi xoa khoi DB
        OK=0; BAD=0
        while IFS= read -r t; do
            [ -n "$t" ] || continue
            if "$MYSQLDUMP" -h127.0.0.1 -uroot --single-transaction --add-drop-table \
                    --default-character-set=utf8mb4 "$DB_NAME" "$t" \
                    > "$DB_DIR/tables/$t.sql" 2>"$TMP/dump.err"; then
                OK=$((OK + 1))
            else
                warn "dump that bai bang [$t]: $(tr '\n' ' ' < "$TMP/dump.err")"
                rm -f "$DB_DIR/tables/$t.sql"
                BAD=$((BAD + 1))
            fi
        done <<< "$TABLES"
        log "  da dump $OK bang (that bai: $BAD)"
        commit_and_push "$DB_DIR" database "sync database: dump $OK bang"
    fi
fi

# ============================================================================= 
echo
if [ "$FAIL" -eq 0 ]; then
    log "HOAN TAT — ca 3 nhanh dong bo."
else
    log "CO CANH BAO/LOI — xem lai o tren (FAIL=$FAIL)."
fi
exit "$FAIL"
