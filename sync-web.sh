#!/usr/bin/env bash
# sync-web.sh — cap nhat rieng nhanh "web" tren GitHub.
#   Chay:  bash sync-web.sh
#   Doi nguon web:  SYNC_WEB_SRC="/c/duong/dan/khac" bash sync-web.sh
# Lam dung viec nhu buoc [2/3] cua sync-all.sh (thay toan bo noi dung, giu .git)
# nhung bang git worktree -> khong phai clone lai repo 2,4GB (nhanh hon nhieu).
set -u
REPO="${SYNC_REPO:-/c/Users/Administrator/Downloads/Dragonballsaga}"
SRC="${SYNC_WEB_SRC:-/c/xampp/htdocs/nrokura}"
WT=/tmp/bmweb
STAMP="$(date '+%F %T')"

don_dep() {
    cd "$REPO" 2>/dev/null || return 0
    git worktree remove --force "$WT" >/dev/null 2>&1 || true
    rm -rf "$WT"
    git branch -D web-sync >/dev/null 2>&1 || true
}
trap don_dep EXIT

cd "$REPO" || { echo "[web] khong vao duoc repo"; exit 1; }
don_dep
rm -rf "$WT"

git worktree add -q -f "$WT" -b web-sync origin/web || { echo "[web] tao worktree THAT BAI"; exit 1; }
cd "$WT" || exit 1

find . -mindepth 1 -maxdepth 1 ! -name .git -exec rm -rf {} +
cp -a "$SRC/." . || { echo "[web] copy source THAT BAI"; exit 1; }

git add -A
N=$(git diff --cached --name-only | wc -l)
echo "[web] so file thay doi: $N"
git diff --cached --name-only | sed 's/^/  /' | head -25

if git diff --cached --quiet; then
    echo "[web] khong co gi doi — bo qua"
    exit 0
fi

git commit -q -m "$(cat <<EOF
sync web: cap nhat Web Nro KOL — bao hiem popup khong khoa trang ($N file thay doi, $STAMP)

🤖 Generated with Codebuff
Co-Authored-By: Codebuff <noreply@codebuff.com>
EOF
)" || { echo "[web] commit THAT BAI"; exit 1; }
echo "[web] commit $(git rev-parse --short HEAD)"

if git push origin HEAD:refs/heads/web 2>&1 | tail -6; then
    echo "[web] DA PUSH nhánh web"
else
    echo "[web] PUSH THAT BAI"
    exit 1
fi
echo "XONG"
