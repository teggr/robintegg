# Quick Fix Checklist
## "Java UI in 2026: The Complete Developer's Guide"

Use this checklist to quickly address all issues found in the article review.

---

## 🔴 CRITICAL (Must Fix Before Publication)

### 1. Fix NetBeans Platform URL (Line 189)
- [ ] **Current:** `https://netbeans.apache.org/platform/` (404 Error)
- [ ] **Change to:** `https://netbeans.apache.org/`
- [ ] **Time:** 30 seconds

### 2. Add Citations or Qualify User/Market Claims
- [ ] **Line 71 & 621:** Physics Wallah (17M users)
  - Add citation or change to: "reported to have 17M users"
  
- [ ] **Line 621:** Feres taxi app (1M+ downloads)
  - Add Play Store/App Store link OR change to: "reported to have 1M+ downloads"
  
- [ ] **Line 621:** Markaz e-commerce (5M+ users)
  - Add citation or change to: "reported to have 5M+ users"
  
- [ ] **Line 394:** Wicket "10% of enterprise Java developers"
  - Add source citation OR remove specific percentage and use: "widely used in enterprise"
  
- [ ] **Line 166:** NetBeans "7 million lines of code and 50,000+ files"
  - Add citation to official NetBeans statistics OR remove specific numbers

**Time:** 30-60 minutes (depending on research needed)

### 3. Verify PrimeFaces URL (Line 441)
- [ ] **URL:** `https://www.primefaces.org/`
- [ ] Test in browser (currently returns 403, may be temporary)
- [ ] If broken, find alternative URL from PrimeFaces documentation
- [ ] **Time:** 5 minutes

---

## 🟡 SHOULD FIX (Quality Improvements)

### 4. Add Missing Import to NetBeans Example (Line ~187)
```java
// Add this import:
import java.awt.BorderLayout;
```
- [ ] Add `import java.awt.BorderLayout;` to NetBeans code example
- [ ] **Time:** 1 minute

### 5. Verify SnapKit Import (Line ~252)
- [ ] Check if SnapKit example needs: `import snap.gfx.Font;`
- [ ] Add if needed for compilation
- [ ] **Time:** 2 minutes

---

## 💡 OPTIONAL (Nice to Have)

### 6. Add Version Age Note for j2html (Line 555)
- [ ] Current: "Last Release: v1.6.0"
- [ ] Change to: "Last Release: v1.6.0 (June 2022, current stable)"
- [ ] **Time:** 30 seconds

### 7. Host FlatLaf Image Locally (Line 126)
- [ ] Download: `https://www.formdev.com/flatlaf/images/themes/dark.png`
- [ ] Save to: `website/_static/images/flatlaf-dark-theme.png`
- [ ] Change reference to: `{{site.baseurl}}/images/flatlaf-dark-theme.png`
- [ ] **Time:** 2 minutes

### 8. Add Disclaimer About Version Currency
Add at the beginning or end:
```markdown
*Version information and framework details current as of February 2026.*
```
- [ ] Add version currency disclaimer
- [ ] **Time:** 30 seconds

### 9. Verify Vaadin 25 Exists
- [ ] Check Vaadin release notes to confirm version 25
- [ ] Update if needed
- [ ] **Time:** 5 minutes

---

## Quick Edit Guide

### File Location
```
website/_posts/2026-02-08-java-ui-in-2026-the-complete-developers-guide.md
```

### Line Number Reference
| Issue | Line(s) | Section |
|-------|---------|---------|
| NetBeans URL | 189 | Desktop Frameworks |
| Physics Wallah (1st) | 71 | Compose Desktop |
| NetBeans imports | ~187 | Desktop Frameworks |
| Swing-Tree | 133-157 | Desktop Frameworks |
| SnapKit | 229-253 | Desktop Frameworks |
| Wicket market share | 394 | Web Frameworks |
| PrimeFaces URL | 441 | Web Frameworks |
| j2html version | 555 | Web Frameworks |
| Physics Wallah (2nd) | 621 | Mobile Frameworks |
| Feres taxi | 621 | Mobile Frameworks |
| Markaz e-commerce | 621 | Mobile Frameworks |

---

## Testing After Fixes

### 1. Test All URLs
```bash
# Test NetBeans URL
curl -I https://netbeans.apache.org/

# Test PrimeFaces URL (in browser)
open https://www.primefaces.org/
```

### 2. Validate Code Examples
- [ ] Copy-paste NetBeans example and verify imports work
- [ ] Copy-paste SnapKit example and verify imports work

### 3. Build Website Locally
```bash
cd website
mvn clean test exec:java
cd target/site
jwebserver
```

### 4. Visual Check
- [ ] Open http://localhost:8000 in browser
- [ ] Navigate to the article
- [ ] Verify all images load
- [ ] Verify all links work
- [ ] Check code examples display correctly

---

## Estimated Total Time

| Priority | Time Required |
|----------|--------------|
| 🔴 Critical | 40-70 minutes |
| 🟡 Should Fix | 5 minutes |
| 💡 Optional | 10 minutes |
| **TOTAL** | **55-85 minutes** |

---

## Sign-Off Checklist

Before publishing, verify:
- [ ] All critical issues fixed
- [ ] NetBeans URL works
- [ ] All user/market claims have citations OR are qualified
- [ ] PrimeFaces URL verified
- [ ] Code examples have all imports
- [ ] Website builds without errors
- [ ] Article displays correctly in browser
- [ ] All links tested and working

---

**Quick Reference:**
- Full review: `ARTICLE_REVIEW_REPORT.md`
- Article file: `website/_posts/2026-02-08-java-ui-in-2026-the-complete-developers-guide.md`
- Date: February 7, 2026
