const fs = require('fs');
const path = require('path');
const { spawn } = require('child_process');

const dir = path.join(__dirname, '../../website/_feeds');

fs.readdir(dir, (err, files) => {
  if (err) {
    console.error('Error reading directory:', err);
    process.exit(1);
  }

  const mdFiles = files.filter(f => f.endsWith('.md'));

  console.log(`Found ${mdFiles.length} .md files`);

  let completed = 0;
  const total = mdFiles.length;

  mdFiles.forEach(file => {
    const fullPath = path.join(dir, file);
    const child = spawn('node', ['tools/feed-cleaner/update-thumbnail.js', fullPath], { stdio: 'pipe' });

    child.stdout.on('data', (data) => {
      process.stdout.write(`${file}: ${data}`);
    });

    child.stderr.on('data', (data) => {
      process.stderr.write(`${file}: ${data}`);
    });

    child.on('close', (code) => {
      completed++;
      console.log(`${file} completed with code ${code}`);
      if (completed === total) {
        console.log('All updates completed');
      }
    });
  });
});
