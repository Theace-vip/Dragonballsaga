<div class="d-flex">
  <nav class="flex-shrink-0 bg-dark text-white p-3 vh-100" style="width:260px; position:fixed; left:0; top:0; overflow-y:auto;">
    <h4 class="mb-1">Quan Ly Game</h4>
    <?php if (!empty($PANEL_DB_NAME)): ?>
      <div class="mb-3"><span class="badge bg-success">DB: <?= htmlspecialchars($PANEL_DB_NAME) ?></span>
      <?php if (!empty($PANEL_GUEST)): ?><span class="badge bg-warning text-dark">guest</span><?php endif; ?></div>
    <?php endif; ?>
    <ul class="nav nav-pills flex-column gap-1">
      <li class="nav-item"><a class="nav-link text-white" href="index.php">Tong quan</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="server.php">Thong so Server</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="bosses.php">Boss</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="map.php">Map &amp; Mob</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="boss.php">Boss Override &amp; Drop</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="events.php">Su kien &amp; Ty le</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="power.php">Suc manh</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="giftcode.php">Giftcode</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="shop.php">Shop</a></li>
      <li><hr class="border-secondary"></li>
      <li class="nav-item"><a class="nav-link text-white-50" href="manage.php?table=account"> Account (cu)</a></li>
      <li class="nav-item"><a class="nav-link text-white-50" href="manage.php?table=player"> Player (cu)</a></li>
      <li class="nav-item"><a class="nav-link text-white-50" href="manage.php?table=giftcode"> Giftcode (cu)</a></li>
    </ul>
  </nav>
  <main class="flex-grow-1" style="margin-left:260px;">
