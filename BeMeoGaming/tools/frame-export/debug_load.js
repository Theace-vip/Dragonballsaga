// Debug: mo viewer :5750, goi loadSet('g13_houtu2'), in stack day du
const puppeteer = require('puppeteer-core');
(async () => {
  const browser = await puppeteer.launch({
    executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe',
    headless: true,
    args: ['--enable-unsafe-swiftshader', '--use-gl=angle', '--use-angle=swiftshader', '--no-sandbox', '--disable-dev-shm-usage'],
  });
  try {
    const page = await browser.newPage();
    page.on('pageerror', (e) => console.log('[pageerror]', e.stack || e.message));
    page.on('console', (m) => console.log('[console:' + m.type() + ']', m.text()));
    page.on('requestfailed', (r) => console.log('[reqfail]', r.url(), r.failure() && r.failure().errorText));
    page.on('response', (r) => { if (r.status() >= 400) console.log('[http ' + r.status() + ']', r.url()); });
    await page.goto('http://localhost:5750/', { waitUntil: 'domcontentloaded', timeout: 30000 });
    await page.waitForFunction('typeof sets !== "undefined" && sets.length > 0', { timeout: 20000 });
    const out = await page.evaluate(async () => {
      const item = sets.find((s) => s.name === 'g13_houtu2');
      try {
        await loadSet(item);
        return { status: document.getElementById('status').textContent, hasSkeleton: !!skeleton };
      } catch (e) {
        return { threw: e.stack || e.message };
      }
    });
    console.log('RESULT:', JSON.stringify(out, null, 1));
  } finally {
    await browser.close().catch(() => {});
  }
})().catch((e) => { console.error(e); process.exit(1); });
