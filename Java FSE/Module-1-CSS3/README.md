# Local Community Event Portal — CSS3 Project

## Project Description

Local Community Event Portal (CivicPulse) is a production-grade, fully responsive website built to demonstrate all core CSS3 concepts from Module-1. The project is themed around a real-world community events platform for Chennai, Tamil Nadu, and covers every required CSS3 technique across a complete multi-section webpage.

---

## Features

- Fixed responsive navigation bar with hamburger menu for mobile
- Full-viewport hero section with background image, gradient overlay, and animated CTA buttons
- Six event cards with hover lift and scale effects, coloured banners, and tag badges
- Styled event schedule table with zebra striping, border-collapse, and badge labels
- Community news section using CSS Multi-column Layout
- Masonry-style image gallery using CSS Grid with hover zoom and caption reveal animations
- Modern registration form with focus ring effects, styled inputs, and grouped layout
- Footer with four-column grid, social icon buttons, and contact links
- Smooth scroll, keyframe animations, and micro-interaction transitions throughout

---

## File Structure

```
community-portal/
├── index.html      — Full semantic HTML5 markup
├── style.css       — All CSS3 styling (external stylesheet)
└── README.md       — Project documentation
```

---

## CSS Concepts Covered

### CSS Cascade Types
| Type | Where Used |
|---|---|
| Inline CSS | `style="background: linear-gradient(…)"` on card banners |
| Internal CSS | `<style>` block in `<head>` for `.inline-demo-badge` |
| External CSS | `style.css` linked via `<link rel="stylesheet">` |

### Selectors
| Selector | Example |
|---|---|
| Universal | `* { box-sizing: border-box; }` |
| Element | `body`, `img`, `a`, `ul`, `h1` |
| ID | `#home`, `#events`, `#gallery`, `#register` |
| Class | `.hero`, `.event-card`, `.schedule-table`, `.gallery-item` |
| Grouping | `h1, h2, h3, h4 { font-family: … }` |

### Colors and Backgrounds
- HEX colors: `#c0392b`, `#1a6b5c`, `#1c1208`, `#faf7f2`
- RGBA colors: `rgba(28, 18, 8, 0.88)`, `rgba(255, 255, 255, 0.75)`
- Linear gradients: hero overlay, card banners, hero title gradient
- Background image: hero section with `background-image: url(…)`
- `background-attachment: fixed` for parallax-style hero
- `background-size: cover` and `background-position: center`

### Typography
- Google Fonts: `Cormorant Garamond` (display), `Outfit` (body)
- `font-family`, `font-size` (rem, clamp), `font-weight` (300–700)
- `font-style: italic` on hero title emphasis
- `text-transform: uppercase` on labels and nav
- `letter-spacing` on eyebrows, badges, buttons
- `line-height` set globally and per component

### Links and Lists
- `a:hover`, `a:visited`, `a:active` states styled throughout
- Navigation links with `::after` pseudo-element underline animation
- `.active-link` class for current page indicator
- `.benefits-list` and `.card-meta` custom-styled lists

### Table Styling
- `border-collapse: collapse`
- Custom `border` on `th` and `td`
- `padding` on all cells
- Zebra striping with `:nth-child(even)` and `:nth-child(odd)`
- `background-color` on `thead` and hover rows
- `text-align: center` on all cells
- Category badge labels inside cells

### Box Model
- `margin`, `padding` used across all components
- `border` and `border-radius` on cards, inputs, buttons
- `outline` with `outline-color` on hovered event cards
- `display: block`, `display: flex`, `display: grid`, `display: inline-block`, `display: inline-flex`
- `visibility` controlled via animated opacity transitions

### Layout
- **Flexbox**: header, nav, hero CTA group, card meta, benefits list, social row, footer bottom
- **CSS Grid**: events section (`repeat(3, 1fr)`), gallery (`repeat(3, 1fr)` with `span`), register layout, footer inner
- **Multi-column**: news section (`column-count: 2`, `column-gap`, `column-rule`)

### Responsive Design — Media Queries
| Breakpoint | Layout Changes |
|---|---|
| `≥ 1025px` | 3-column events grid, 3-column gallery |
| `769px – 1024px` | 2-column events, 2-column gallery, 2-column footer |
| `≤ 768px` | Single-column events, hamburger nav, 1-column news, 1-column footer |
| `≤ 480px` | Single-column gallery, reduced font sizes |

### Animations and Transitions
- `@keyframes fade-up` — hero content entrance animation
- `@keyframes bounce` — scroll hint arrow
- `@keyframes spin-slow` — logo diamond rotation
- `transition` on all interactive elements (cards, buttons, nav links, inputs)
- `transform: translateY()` and `scale()` on card hover
- `transform: scale(1.08)` on gallery image hover
- Caption `opacity` and `translateY` reveal on gallery hover
- `box-shadow` transition on buttons and cards
- Hamburger menu `transform: rotate()` animation on toggle

---

## Technologies Used

- HTML5 (semantic elements: `header`, `nav`, `section`, `article`, `figure`, `footer`)
- CSS3 (external, internal, inline)
- Google Fonts API
- Unsplash (royalty-free images)
- Vanilla JavaScript (hamburger toggle via CSS checkbox hack + minor inline handlers)

---

## How to Run

1. Download or clone the project folder.
2. Ensure `index.html` and `style.css` are in the same directory.
3. Open `index.html` in any modern browser.
4. An internet connection is required for Google Fonts and Unsplash images.

---

## Author

**Name:** Ragul SJ
**Email:** sjragul555@gmail.com

---

## Copyright

&copy; 2026 CivicPulse — Local Community Event Portal. All rights reserved.
