const fs=require('fs'),path=require('path');
const roots=process.argv[2].split(';');
const keys=process.argv[3].split(';');
function walk(d,out){for(const e of fs.readdirSync(d,{withFileTypes:true})){const p=path.join(d,e.name);if(e.isDirectory()){if(e.name==='.git'||e.name==='Library'||e.name==='obj'||e.name==='bin'||e.name==='build')continue;walk(p,out);}else if(/\.(cs|java)$/i.test(e.name)){out.push(p);}}return out;}
for(const root of roots){
  for(const f of walk(root,[])){
    const txt=fs.readFileSync(f,'utf8').split('\n');
    txt.forEach((line,i)=>{for(const k of keys){if(line.toLowerCase().includes(k.toLowerCase())){console.log(`${f}:${i+1}: ${line.trim().slice(0,200)}`);break;}}});
  }
}
