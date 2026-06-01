'use strict';

class EventModel {
  constructor({ id, name, category, date, time, location, seats, availableSeats, fee, description, image, organizer }) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.date = date;
    this.time = time;
    this.location = location;
    this.seats = seats;
    this.availableSeats = availableSeats;
    this.fee = fee;
    this.description = description;
    this.image = image;
    this.organizer = organizer;
  }

  get isFull() {
    return this.availableSeats <= 0;
  }

  get isLowStock() {
    return this.availableSeats > 0 && this.availableSeats <= 10;
  }

  get formattedFee() {
    return this.fee === 0 ? 'Free' : `₹${this.fee}`;
  }

  get formattedDate() {
    const d = new Date(`${this.date}T${this.time}`);
    return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' }) +
      ' · ' + d.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
  }

  decrementSeat() {
    if (!this.isFull) this.availableSeats -= 1;
  }
}

EventModel.prototype.getSeatStatus = function () {
  if (this.isFull) return { label: 'Full', cls: 'seats-full' };
  if (this.isLowStock) return { label: `${this.availableSeats} left`, cls: 'seats-low' };
  return { label: `${this.availableSeats} seats`, cls: 'seats-ok' };
};

const createRegistrationTracker = () => {
  let count = 0;
  const log = [];

  return {
    register(userName, eventName) {
      count += 1;
      log.push({ userName, eventName, at: new Date().toISOString() });
    },
    getCount() { return count; },
    getLog() { return [...log]; },
    reset() { count = 0; log.length = 0; }
  };
};

const registrationTracker = createRegistrationTracker();

let allEvents = [];
let filteredEvents = [];
let currentSearch = '';
let currentCategory = 'All';

const STORAGE_KEY_PREF = 'civicpulse_prefs';
const SESSION_KEY = 'civicpulse_session';

const getEl = (id) => document.getElementById(id);
const qs = (sel, ctx = document) => ctx.querySelector(sel);

const showToast = (message, type = 'info') => {
  const container = getEl('toast-container');
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;
  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transition = 'opacity 0.4s ease';
    setTimeout(() => toast.remove(), 400);
  }, 3500);
};

const updateDateTime = () => {
  const now = new Date();
  const hours = now.getHours();
  let greeting = 'Good evening';
  if (hours < 12) greeting = 'Good morning';
  else if (hours < 17) greeting = 'Good afternoon';

  const timeStr = now.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  const dateStr = now.toLocaleDateString('en-IN', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });

  const greetEl = getEl('greeting-line');
  const dtEl = getEl('datetime-display');

  if (greetEl) greetEl.textContent = greeting;
  if (dtEl) dtEl.textContent = `${dateStr} · ${timeStr}`;
};

const updateStats = () => {
  const totalEvents = allEvents.length;
  const totalReg = registrationTracker.getCount();
  const totalSeats = allEvents.reduce((acc, ev) => acc + ev.availableSeats, 0);
  const popular = allEvents.reduce((max, ev) =>
    (ev.seats - ev.availableSeats) > (max.seats - max.availableSeats) ? ev : max, allEvents[0]);

  const evCount = getEl('stat-events-count');
  const regCount = getEl('stat-reg-count');
  const seatsCount = getEl('stat-seats-count');
  const popName = getEl('stat-popular-name');

  if (evCount) evCount.textContent = totalEvents;
  if (regCount) regCount.textContent = totalReg;
  if (seatsCount) seatsCount.textContent = totalSeats;
  if (popName && popular) popName.textContent = popular.name;
};

const populateEventSelect = (events) => {
  const select = getEl('f-event');
  if (!select) return;
  const placeholder = select.querySelector('option[value=""]');
  select.innerHTML = '';
  if (placeholder) select.appendChild(placeholder);
  events.forEach(ev => {
    const opt = document.createElement('option');
    opt.value = ev.id;
    opt.textContent = `${ev.name} (${ev.isFull ? 'Full' : `${ev.availableSeats} seats`})`;
    if (ev.isFull) opt.disabled = true;
    select.appendChild(opt);
  });
};

const createEventCard = (ev) => {
  const { label, cls } = ev.getSeatStatus();
  const card = document.createElement('article');
  card.className = 'event-card';
  card.dataset.id = ev.id;

  card.innerHTML = `
    <div class="event-card-img">
      <img src="${ev.image}" alt="${ev.name}" loading="lazy" />
      <span class="event-card-cat cat-${ev.category}">${ev.category}</span>
      <span class="seats-badge ${cls}">${label}</span>
    </div>
    <div class="event-card-body">
      <h3 class="event-card-name">${ev.name}</h3>
      <div class="event-card-meta">
        <span>📅 ${ev.formattedDate}</span>
        <span>📍 ${ev.location}</span>
        <span>🏛 ${ev.organizer}</span>
      </div>
      <p class="event-card-desc">${ev.description}</p>
      <div class="event-card-footer">
        <span class="event-fee">${ev.formattedFee}</span>
        <button class="register-event-btn" data-id="${ev.id}" ${ev.isFull ? 'disabled' : ''}>
          ${ev.isFull ? 'Sold Out' : 'Register'}
        </button>
      </div>
    </div>
  `;

  const regBtn = qs('.register-event-btn', card);
  regBtn.addEventListener('click', () => {
    const eventSelect = getEl('f-event');
    if (eventSelect) {
      eventSelect.value = ev.id;
      document.querySelector('#register-section').scrollIntoView({ behavior: 'smooth' });
    }
  });

  return card;
};

const renderEvents = (events) => {
  const grid = getEl('events-grid');
  const noResults = getEl('no-results');
  const countLabel = getEl('result-count-label');

  if (!grid) return;
  grid.innerHTML = '';

  if (events.length === 0) {
    noResults.style.display = 'block';
    grid.style.display = 'none';
  } else {
    noResults.style.display = 'none';
    grid.style.display = 'grid';
    events.forEach((ev, i) => {
      const card = createEventCard(ev);
      card.style.animationDelay = `${i * 0.06}s`;
      card.style.animation = 'fade-up 0.5s ease both';
      grid.appendChild(card);
    });
  }

  if (countLabel) {
    countLabel.textContent = events.length === allEvents.length
      ? `Showing all ${events.length} events`
      : `${events.length} of ${allEvents.length} events`;
  }
};

const searchEvents = (query = '') => {
  currentSearch = query.toLowerCase().trim();
  applyFilters();
};

const filterEvents = (category = 'All') => {
  currentCategory = category;
  applyFilters();
};

const applyFilters = () => {
  filteredEvents = allEvents.filter(ev => {
    const matchesSearch = currentSearch === '' ||
      ev.name.toLowerCase().includes(currentSearch) ||
      ev.location.toLowerCase().includes(currentSearch) ||
      ev.organizer.toLowerCase().includes(currentSearch);
    const matchesCategory = currentCategory === 'All' || ev.category === currentCategory;
    return matchesSearch && matchesCategory;
  });
  renderEvents(filteredEvents);
};

const addEvent = ({ name, category, date, time, location, seats, fee, description, image, organizer } = {}) => {
  const newId = allEvents.length > 0 ? Math.max(...allEvents.map(e => e.id)) + 1 : 1;
  const newEvent = new EventModel({
    id: newId,
    name: name || 'New Event',
    category: category || 'Cultural',
    date: date || new Date().toISOString().slice(0, 10),
    time: time || '10:00',
    location: location || 'Chennai',
    seats: seats || 50,
    availableSeats: seats || 50,
    fee: fee || 0,
    description: description || 'A community event.',
    image: image || 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=600&h=400&fit=crop',
    organizer: organizer || 'CivicPulse'
  });
  allEvents.push(newEvent);
  populateEventSelect(allEvents);
  applyFilters();
  updateStats();
  console.log('[addEvent] New event added:', newEvent);
  return newEvent;
};

const registerUser = ({ name, email, phone, eventId } = {}) => {
  const ev = allEvents.find(e => e.id === parseInt(eventId));
  if (!ev) throw new Error('Event not found');
  if (ev.isFull) throw new Error(`${ev.name} is fully booked.`);

  ev.decrementSeat();
  registrationTracker.register(name, ev.name);

  const sessionData = JSON.parse(sessionStorage.getItem(SESSION_KEY) || '{}');
  sessionData.lastRegistration = { name, email, phone, eventName: ev.name, at: new Date().toISOString() };
  sessionStorage.setItem(SESSION_KEY, JSON.stringify(sessionData));

  updateStats();
  populateEventSelect(allEvents);
  renderEvents(filteredEvents.length < allEvents.length ? filteredEvents : allEvents);
  updateSessionInfo();

  console.log(`[registerUser] ${name} registered for ${ev.name}. Seats left: ${ev.availableSeats}`);
  return { success: true, event: ev, name };
};

const validateForm = () => {
  let valid = true;
  const fields = [
    { id: 'f-name', errId: 'err-name', label: 'Full name', test: (v) => v.trim().length >= 2 },
    { id: 'f-email', errId: 'err-email', label: 'Valid email', test: (v) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) },
    { id: 'f-phone', errId: 'err-phone', label: 'Phone (10+ digits)', test: (v) => /^[\d\s+\-()]{10,}$/.test(v) },
    { id: 'f-event', errId: 'err-event', label: 'Event selection', test: (v) => v !== '' }
  ];

  fields.forEach(({ id, errId, label, test }) => {
    const input = getEl(id);
    const errEl = getEl(errId);
    const value = input ? input.value : '';
    if (!test(value)) {
      if (errEl) errEl.textContent = `${label} is required`;
      if (input) input.classList.add('input-error');
      valid = false;
    } else {
      if (errEl) errEl.textContent = '';
      if (input) input.classList.remove('input-error');
    }
  });

  return valid;
};

const savePreferences = () => {
  const prefs = { search: currentSearch, category: currentCategory };
  try {
    localStorage.setItem(STORAGE_KEY_PREF, JSON.stringify(prefs));
    showToast('Preferences saved!', 'success');
    console.log('[localStorage] Preferences saved:', prefs);
  } catch (e) {
    console.error('[localStorage] Save failed:', e);
    showToast('Could not save preferences.', 'error');
  }
};

const loadPreferences = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY_PREF);
    if (!raw) return;
    const { search = '', category = 'All' } = JSON.parse(raw);
    currentSearch = search;
    currentCategory = category;

    const searchInput = getEl('search-input');
    const filterSelect = getEl('category-filter');
    if (searchInput) searchInput.value = search;
    if (filterSelect) filterSelect.value = category;

    const clearBtn = getEl('search-clear');
    if (clearBtn && search) clearBtn.classList.add('visible');

    console.log('[localStorage] Preferences loaded:', { search, category });
  } catch (e) {
    console.error('[localStorage] Load failed:', e);
  }
};

const clearPreferences = () => {
  try {
    localStorage.removeItem(STORAGE_KEY_PREF);
    currentSearch = '';
    currentCategory = 'All';
    const searchInput = getEl('search-input');
    const filterSelect = getEl('category-filter');
    if (searchInput) searchInput.value = '';
    if (filterSelect) filterSelect.value = 'All';
    const clearBtn = getEl('search-clear');
    if (clearBtn) clearBtn.classList.remove('visible');
    applyFilters();
    showToast('Preferences cleared.', 'info');
    console.log('[localStorage] Preferences cleared');
  } catch (e) {
    console.error('[localStorage] Clear failed:', e);
  }
};

const updateSessionInfo = () => {
  const sessionBox = getEl('session-info');
  if (!sessionBox) return;
  try {
    const sessionData = JSON.parse(sessionStorage.getItem(SESSION_KEY) || '{}');
    if (sessionData.lastRegistration) {
      const { name, eventName } = sessionData.lastRegistration;
      sessionBox.textContent = `✔ ${name} — last registered for "${eventName}" this session`;
    }
  } catch (e) {
    console.error('[sessionStorage] Read failed:', e);
  }
};

const fetchEventsWithPromise = () => {
  const spinner = getEl('loading-spinner');
  const grid = getEl('events-grid');
  if (spinner) spinner.style.display = 'flex';
  if (grid) grid.style.display = 'none';

  return fetch('./events.json')
    .then(response => {
      if (!response.ok) throw new Error(`HTTP ${response.status}: Failed to load events`);
      return response.json();
    })
    .then(data => {
      allEvents = data.map(ev => new EventModel(ev));
      filteredEvents = [...allEvents];
      if (spinner) spinner.style.display = 'none';
      if (grid) grid.style.display = 'grid';
      return allEvents;
    })
    .catch(err => {
      console.error('[fetch] Error loading events.json:', err);
      if (spinner) spinner.style.display = 'none';
      showToast('Could not load events. Make sure events.json is present.', 'error');
      throw err;
    });
};

const initApp = async () => {
  try {
    console.log('[init] CivicPulse — Community Event Portal starting…');

    updateDateTime();
    setInterval(updateDateTime, 1000);

    await fetchEventsWithPromise();

    loadPreferences();
    applyFilters();
    updateStats();
    populateEventSelect(allEvents);
    updateSessionInfo();

    console.log(`[init] ${allEvents.length} events loaded.`);
    console.log('[init] Events by category:', allEvents.reduce((acc, ev) => {
      acc[ev.category] = (acc[ev.category] || 0) + 1;
      return acc;
    }, {}));

    const totalFees = allEvents.map(ev => ev.fee).reduce((a, b) => a + b, 0);
    const avgFee = (totalFees / allEvents.length).toFixed(2);
    console.log(`[init] Average event fee: ₹${avgFee}`);

    const categorySpread = [...new Set(allEvents.map(ev => ev.category))];
    console.log('[init] Categories:', categorySpread);

    attachEventListeners();
    attachJQueryFeatures();

  } catch (err) {
    console.error('[init] Initialisation error:', err);
  }
};

const attachEventListeners = () => {
  const searchInput = getEl('search-input');
  const searchClear = getEl('search-clear');
  const categoryFilter = getEl('category-filter');
  const savePrefBtn = getEl('save-pref-btn');
  const clearPrefBtn = getEl('clear-pref-btn');
  const registerForm = getEl('register-form');
  const resetBtn = getEl('reset-btn');

  if (searchInput) {
    searchInput.addEventListener('input', (e) => {
      const val = e.target.value;
      searchEvents(val);
      if (searchClear) {
        val.length > 0
          ? searchClear.classList.add('visible')
          : searchClear.classList.remove('visible');
      }
    });

    searchInput.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') {
        searchInput.value = '';
        searchEvents('');
        if (searchClear) searchClear.classList.remove('visible');
      }
    });

    searchInput.addEventListener('blur', () => {
      console.log('[search] User left search field. Current query:', currentSearch);
    });
  }

  if (searchClear) {
    searchClear.onclick = () => {
      if (searchInput) searchInput.value = '';
      searchEvents('');
      searchClear.classList.remove('visible');
    };
  }

  if (categoryFilter) {
    categoryFilter.onchange = (e) => {
      filterEvents(e.target.value);
    };
  }

  if (savePrefBtn) {
    savePrefBtn.addEventListener('click', savePreferences);
  }

  if (clearPrefBtn) {
    clearPrefBtn.addEventListener('click', clearPreferences);
  }

  if (registerForm) {
    registerForm.addEventListener('submit', (e) => {
      e.preventDefault();

      const formMsg = getEl('form-message');
      if (formMsg) { formMsg.className = 'form-message'; formMsg.textContent = ''; }

      if (!validateForm()) {
        if (formMsg) {
          formMsg.className = 'form-message error';
          formMsg.textContent = 'Please fix the errors above before submitting.';
        }
        return;
      }

      const name = getEl('f-name').value.trim();
      const email = getEl('f-email').value.trim();
      const phone = getEl('f-phone').value.trim();
      const eventId = getEl('f-event').value;

      try {
        const result = registerUser({ name, email, phone, eventId });
        if (formMsg) {
          formMsg.className = 'form-message success';
          formMsg.textContent = `✔ ${result.name}, you're registered for "${result.event.name}"! Seats remaining: ${result.event.availableSeats}.`;
        }
        showToast(`Registered for ${result.event.name}!`, 'success');
        registerForm.reset();
        populateEventSelect(allEvents);
      } catch (err) {
        if (formMsg) {
          formMsg.className = 'form-message error';
          formMsg.textContent = `Registration failed: ${err.message}`;
        }
        showToast(err.message, 'error');
        console.error('[registerUser] Error:', err);
      }
    });
  }

  if (resetBtn) {
    resetBtn.addEventListener('click', () => {
      if (registerForm) registerForm.reset();
      const formMsg = getEl('form-message');
      if (formMsg) { formMsg.className = 'form-message'; formMsg.textContent = ''; }
      ['f-name', 'f-email', 'f-phone', 'f-event'].forEach(id => {
        const el = getEl(id);
        if (el) el.classList.remove('input-error');
      });
      ['err-name', 'err-email', 'err-phone', 'err-event'].forEach(id => {
        const el = getEl(id);
        if (el) el.textContent = '';
      });
    });
  }

  const switchDemo = (val) => {
    switch (val) {
      case 'Cultural': console.log('[switch] Filtering Cultural events'); break;
      case 'Technology': console.log('[switch] Filtering Technology events'); break;
      case 'Wellness': console.log('[switch] Filtering Wellness events'); break;
      case 'Food': console.log('[switch] Filtering Food events'); break;
      case 'Music': console.log('[switch] Filtering Music events'); break;
      default: console.log('[switch] All categories selected');
    }
  };

  if (categoryFilter) {
    categoryFilter.addEventListener('change', (e) => switchDemo(e.target.value));
  }

  for (let i = 0; i < allEvents.length; i++) {
    const ev = allEvents[i];
    if (ev.availableSeats < 15) {
      console.warn(`[for loop] Low seats warning: "${ev.name}" has only ${ev.availableSeats} seats.`);
    }
  }

  allEvents.forEach((ev, idx) => {
    console.log(`[forEach] Event #${idx + 1}: ${ev.name} — ${ev.category}`);
  });

  const techEvents = allEvents.filter(ev => ev.category === 'Technology');
  const eventNames = allEvents.map(ev => ev.name);
  const firstFree = allEvents.find(ev => ev.fee === 0);
  const totalRevenuePotential = allEvents.reduce((acc, ev) => acc + ev.fee * ev.seats, 0);

  console.log('[Array.filter] Technology events:', techEvents.map(e => e.name));
  console.log('[Array.map] All event names:', eventNames);
  console.log('[Array.find] First free event:', firstFree?.name);
  console.log(`[Array.reduce] Total revenue potential: ₹${totalRevenuePotential.toLocaleString('en-IN')}`);

  const { name: firstName, category: firstCat } = allEvents[0] || {};
  const eventsCopy = [...allEvents];
  const greet = (msg, emoji = '👋') => `${emoji} ${msg}`;

  console.log('[Destructuring] First event:', firstName, firstCat);
  console.log('[Spread] Events copy length:', eventsCopy.length);
  console.log('[Default Param]', greet('Welcome to CivicPulse'));
  console.log(`[Template Literal] Portal loaded with ${allEvents.length} events across ${new Set(allEvents.map(e => e.category)).size} categories.`);
};

const attachJQueryFeatures = () => {
  if (typeof $ === 'undefined') {
    console.warn('[jQuery] jQuery not loaded');
    return;
  }

  $('#toggle-stats-btn').on('click', function () {
    const $statsSection = $('#stats-section');
    if ($statsSection.is(':visible')) {
      $statsSection.fadeOut(400);
      $(this).text('View Statistics');
    } else {
      $statsSection.fadeIn(400);
      $(this).text('Hide Statistics');
    }
  });

  $('.register-section').hide().fadeIn(800);

  $(document).on('click', '.register-event-btn', function () {
    const $card = $(this).closest('.event-card');
    $card.css('border-color', 'var(--amber)');
    setTimeout(() => $card.css('border-color', ''), 1200);
  });

  $('#search-input').on('focus', function () {
    $(this).parent('.search-wrap').addClass('focused');
  }).on('blur', function () {
    $(this).parent('.search-wrap').removeClass('focused');
  });

  console.log('[jQuery] jQuery features attached. Version:', $.fn.jquery);
};

document.addEventListener('DOMContentLoaded', initApp);
