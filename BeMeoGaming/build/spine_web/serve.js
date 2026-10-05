// Server tĩnh cho viewer spine online — khong dependency.
// Chay: node serve.js  (mac dinh http://localhost:5760/)
const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 5760; // khong doc env.PORT (sandbox dat PORT=0 -> listen lung tung)
const ROOT = __dirname;
const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.atlas': 'text/plain; charset=utf-8',
  '.svg': 'image/svg+xml',
};

http
  .createServer((req, res) => {
    let p = decodeURIComponent(req.url.split('?')[0]);
    if (p === '/') p = '/index.html';
    const file = path.join(ROOT, path.normalize(p));
    if (!file.startsWith(ROOT) || !fs.existsSync(file) || !fs.statSync(file).isFile()) {
      res.writeHead(404);
      return res.end('404 ' + p);
    }
    res.writeHead(200, {
      'Content-Type': MIME[path.extname(file).toLowerCase()] || 'application/octet-stream',
      'Access-Control-Allow-Origin': '*',
      'Cache-Control': 'no-store',
    });
    fs.createReadStream(file).pipe(res);
  })
  .listen(PORT, () => console.log('spine web: http://localhost:' + PORT + '/'));
