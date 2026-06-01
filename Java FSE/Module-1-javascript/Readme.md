# Local Community Event Portal — JavaScript Project

## Project Description

Local Community Event Portal (CivicPulse) is a fully functional, production-grade single-page web application built to demonstrate all core JavaScript concepts from Module-1. The project simulates a real community events platform for Chennai, Tamil Nadu. Events are fetched asynchronously from a local JSON file, rendered dynamically via DOM manipulation, and managed through a complete set of ES6+ JavaScript features, classes, closures, async/await, Fetch API, Local Storage, Session Storage, jQuery, and comprehensive form validation.

---

## File Structure

```
js-portal/
├── index.html       — Semantic HTML5 structure
├── style.css        — Complete CSS3 styling (dark editorial theme)
├── script.js        — All JavaScript logic (ES6+, OOP, async, jQuery)
├── events.json      — 12 realistic community events (Fetch API source)
└── README.md        — Project documentation
```

---

## Features

- Live clock and contextual greeting (morning / afternoon / evening)
- Events fetched asynchronously from `events.json` with loading spinner
- Dynamic event cards rendered via DOM manipulation (`createElement`, `appendChild`)
- Real-time search (by name, location, organizer) with keyboard shortcuts
- Category filter (Cultural, Technology, Wellness, Food, Music)
- Registration form with full field-level validation and error messages
- Seat decrement on successful registration (live update across all cards)
- Toast notification system for all user feedback
- Statistics dashboard (total events, registrations, available seats, most popular event)
- Closure-based registration tracker with in-memory log
- LocalStorage for saving and restoring filter preferences
- SessionStorage for tracking current-session registration history
- jQuery fade animations and toggle interactions
- Console logging demonstrating every array method, destructuring, spread, and more

---

## JavaScript Concepts Covered

### Variables & Data Types
- `let` and `const` used throughout
- Strings, Numbers, Booleans, Arrays, Objects, null, undefined

### Operators & Template Literals
- Arithmetic, comparison, logical, ternary operators
- Template literals with embedded expressions and multi-line strings

### Control Flow
- `if / else` — validation, seat status, greeting logic
- `switch` — category label routing (console demo)
- `for` loop — low seat warning scan
- `forEach` — event name console logging
- Error handling via `try / catch / throw`

### Functions
- `addEvent()` — creates and pushes a new EventModel
- `registerUser()` — validates, decrements seat, stores session
- `searchEvents()` — live name/location/organizer search
- `filterEvents()` — category-based filtering
- Arrow functions used throughout
- Default parameters — `greet(msg, emoji = '👋')`

### Closures
- `createRegistrationTracker()` — returns a closure with private `count` and `log`
- `registrationTracker.register()`, `.getCount()`, `.getLog()`, `.reset()`

### Classes & Prototypes
- `EventModel` class with constructor, getters, and methods
- `EventModel.prototype.getSeatStatus()` — prototype method addition

### Arrays & Array Methods
- `push()` — adding events to `allEvents`
- `filter()` — category and search filtering
- `map()` — extracting event names, computing fee totals
- `find()` — locating first free event, finding by ID
- `reduce()` — total revenue potential, registration count aggregation

### DOM Manipulation
- `getElementById()`, `querySelector()`
- `createElement()`, `appendChild()`, `innerHTML`
- `dataset`, `classList.add/remove/contains`

### Event Handling
- `addEventListener('input')`, `addEventListener('submit')`, `addEventListener('click')`
- `addEventListener('keydown')` — Escape key to clear search
- `addEventListener('blur')` — form field and search feedback
- `onchange` — category filter select
- `onclick` — search clear button
- `preventDefault()` — form submission control

### Local Storage
- `localStorage.setItem()` — save filter preferences
- `localStorage.getItem()` — restore preferences on page load
- `localStorage.removeItem()` — clear preferences

### Session Storage
- `sessionStorage.setItem()` — record last registration per session
- `sessionStorage.getItem()` — display session activity in UI

### Async JavaScript
- `fetch()` with Promise chain (`then`, `catch`)
- `async/await` wrapper in `initApp()`
- Loading spinner shown/hidden during fetch
- Error boundary with user-friendly toast message

### Promises
- `fetchEventsWithPromise()` returns a Promise
- `.then()` to parse JSON and build EventModel instances
- `.catch()` to handle network/parse errors

### Fetch API
- `fetch('./events.json')` reads local JSON
- `response.ok` check before parsing
- JSON parsed into `EventModel` instances

### Form Handling
- Fields: Full Name, Email, Phone, Event Selection
- Regex validation for email and phone
- Field-level error messages with `.input-error` class
- `reset()` button clears form and error states
- Success and error feedback rendered inside form

### ES6+ Modern JavaScript
- `let`, `const` — block scoping
- Arrow functions — `(e) => {}`, `ev => ev.name`
- Destructuring — `const { name, category } = allEvents[0]`
- Spread operator — `const eventsCopy = [...allEvents]`
- Default parameters — `greet(msg, emoji = '👋')`
- Template literals — all dynamic strings
- Optional chaining — `firstFree?.name`
- `Set` — unique category count

### jQuery
- `$('#toggle-stats-btn').on('click', ...)` — button event binding
- `.fadeOut(400)` — hide stats section with animation
- `.fadeIn(400)` — show stats / register section
- `.hide().fadeIn(800)` — initial register section reveal
- `$(document).on('click', '.register-event-btn', ...)` — delegated click
- `.is(':visible')` — visibility check for toggle

### Statistics Dashboard
- Total Events (from `allEvents.length`)
- Total Registrations (from closure tracker)
- Available Seats (via `reduce`)
- Most Popular Event (via `reduce` comparing seats filled)

---

## Technologies Used

| Technology | Purpose |
|---|---|
| HTML5 | Semantic page structure |
| CSS3 | Dark editorial UI, responsive layout |
| JavaScript ES6+ | Full application logic |
| Fetch API | Async JSON loading |
| jQuery 3.7.1 | Fade animations, event delegation |
| Google Fonts | Syne + Literata typography |
| LocalStorage | Persistent filter preferences |
| SessionStorage | Session-scoped registration history |

---

## Installation & Setup

1. Download or clone the project folder.
2. Ensure all five files are in the **same directory**:
   - `index.html`
   - `style.css`
   - `script.js`
   - `events.json`
   - `README.md`
3. Open `index.html` using a local web server (required for `fetch()` to load `events.json`):

   **Option A — VS Code Live Server**
   - Install the "Live Server" extension in VS Code.
   - Right-click `index.html` → "Open with Live Server".

   **Option B — Python HTTP Server**
   ```bash
   cd js-portal
   python3 -m http.server 8080
   # Then open http://localhost:8080 in your browser
   ```

   **Option C — Node.js**
   ```bash
   npx serve .
   ```

> ⚠️ Opening `index.html` directly via `file://` will cause a CORS error on `fetch()`. Always use a local server.

---

## Author

**Name:** Ragul SJ
**Email:** sjragul555@gmail.com

---

## Copyright

&copy; 2026 CivicPulse — Local Community Event Portal. All rights reserved.

