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
