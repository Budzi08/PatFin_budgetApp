import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FontSizeService {
  private readonly storageKey = 'patfin-font-size';
  private size = 16; // px
  private readonly minSize = 12;
  private readonly maxSize = 24;
  
  // Observable for components to subscribe to font size changes
  public fontSizeChanged$ = new Subject<number>();

  constructor() {
    try {
      const stored = localStorage.getItem(this.storageKey);
      if (stored) {
        const num = Number(stored);
        if (!isNaN(num)) this.size = num;
      }
    } catch (e) {
      // ignore localStorage errors
    }
    this.apply();
  }

  increase() {
    if (this.size < this.maxSize) {
      this.size += 2;
      this.saveAndApply();
    }
  }

  decrease() {
    if (this.size > this.minSize) {
      this.size -= 2;
      this.saveAndApply();
    }
  }

  set(sizePx: number) {
    const clamped = Math.min(this.maxSize, Math.max(this.minSize, sizePx));
    this.size = clamped;
    this.saveAndApply();
  }

  get(): number {
    return this.size;
  }

  private saveAndApply() {
    try { localStorage.setItem(this.storageKey, String(this.size)); } catch (e) {}
    this.apply();
    this.fontSizeChanged$.next(this.size);
  }

  private apply() {
    if (typeof document !== 'undefined' && document.documentElement) {
      document.documentElement.style.fontSize = `${this.size}px`;
    }
  }
}
