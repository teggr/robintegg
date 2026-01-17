const fs = require('fs');
const https = require('https');

(async () => {
    const filePath = process.argv[2];
    if (!filePath) {
        console.error('Usage: node update-thumbnail.js <file.md>');
        process.exit(1);
    }

    try {
        const content = fs.readFileSync(filePath, 'utf8');
        const lines = content.split('\n');

        // Find front matter
        let frontMatterStart = -1;
        let frontMatterEnd = -1;
        for (let i = 0; i < lines.length; i++) {
            if (lines[i].trim() === '---') {
                if (frontMatterStart === -1) {
                    frontMatterStart = i;
                } else {
                    frontMatterEnd = i;
                    break;
                }
            }
        }

        if (frontMatterStart === -1 || frontMatterEnd === -1) {
            console.error('No front matter found');
            process.exit(1);
        }

        // Extract site_url
        let siteUrl = null;
        for (let i = frontMatterStart + 1; i < frontMatterEnd; i++) {
            const line = lines[i];
            if (line.startsWith('site_url:')) {
                siteUrl = line.substring(line.indexOf(':') + 1).trim();
                break;
            }
        }

        if (!siteUrl) {
            console.error('site_url not found in front matter');
            process.exit(1);
        }

        console.log(`Fetching ${siteUrl}`);

        // Fetch the HTML
        const fetchOgImage = (url, maxRedirects = 5) => {
            return new Promise((resolve, reject) => {
                https.get(url, (res) => {
                    if (res.statusCode >= 300 && res.statusCode < 400 && res.headers.location) {
                        if (maxRedirects > 0) {
                            return fetchOgImage(res.headers.location, maxRedirects - 1).then(resolve).catch(reject);
                        } else {
                            reject(new Error('Too many redirects'));
                        }
                    }
                    let data = '';
                    res.on('data', (chunk) => {
                        data += chunk;
                    });
                    res.on('end', () => {
                        const ogImageRegex = /<meta[^>]*property="og:image"[^>]*content="([^"]+)"/i;
                        const match = data.match(ogImageRegex);
                        if (match) {
                            resolve(match[1]);
                        } else {
                            resolve(null);
                        }
                    });
                }).on('error', (err) => {
                    reject(err);
                });
            });
        };

        try {
            const ogImageUrl = await fetchOgImage(siteUrl);
            if (ogImageUrl) {
                console.log(`Found og:image: ${ogImageUrl}`);

                // Update thumbnail
                for (let i = frontMatterStart + 1; i < frontMatterEnd; i++) {
                    if (lines[i].startsWith('thumbnail:')) {
                        lines[i] = `thumbnail: ${ogImageUrl}`;
                        break;
                    }
                }

                // Write back
                const newContent = lines.join('\n');
                fs.writeFileSync(filePath, newContent, 'utf8');
                console.log('Thumbnail updated successfully');
            } else {
                console.log('No og:image found, skipping update');
            }
        } catch (err) {
            console.error('Error fetching site:', err.message);
        }
    } catch (err) {
        console.error('Error reading file:', err.message);
        process.exit(1);
    }
})();
