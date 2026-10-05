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
