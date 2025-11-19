import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export type Theme = 'light' | 'dark' | 'high-contrast' | 'deuteranopia' | 'protanopia';

export interface ThemeOption {
  id: Theme;
  name: string;
  description: string;
  icon: string; // emoji or color circle
  primaryColor: string;
}

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly storageKey = 'patfin-theme';
  private currentTheme: Theme = 'light';
  
  public themeChanged$ = new Subject<Theme>();
  
  public themes: ThemeOption[] = [
    {
      id: 'light',
      name: 'Jasny',
      description: 'Standardowy jasny motyw',
      icon: '☀️',
      primaryColor: '#ffffff'
    },
    {
      id: 'dark',
      name: 'Ciemny',
      description: 'Motyw ciemny dla pracy w nocy',
      icon: '🌙',
      primaryColor: '#1a1a1a'
    },
    {
      id: 'high-contrast',
      name: 'Wysoki kontrast',
      description: 'Maksymalny kontrast dla lepszej czytelności',
      icon: '⚫',
      primaryColor: '#000000'
    },
    {
      id: 'deuteranopia',
      name: 'Deuteranopia',
      description: 'Dostosowany dla osób z deuteranopią (ślepota czerwono-zielona)',
      icon: '🔵',
      primaryColor: '#0088cc'
    },
    {
      id: 'protanopia',
      name: 'Protanopia',
      description: 'Dostosowany dla osób z protanopią (brak percepcji czerwieni)',
      icon: '🟡',
      primaryColor: '#ffaa00'
    }
  ];

  constructor() {
    this.loadSavedTheme();
  }

  applyTheme(theme: Theme) {
    // Remove all theme classes
    this.themes.forEach(t => {
      document.body.classList.remove(`theme-${t.id}`);
    });
    
    // Add new theme class
    document.body.classList.add(`theme-${theme}`);
    this.currentTheme = theme;
    
    // Save to localStorage
    try {
      localStorage.setItem(this.storageKey, theme);
    } catch (e) {
      // ignore localStorage errors
    }
    
    // Notify subscribers
    this.themeChanged$.next(theme);
  }

  getCurrentTheme(): Theme {
    return this.currentTheme;
  }

  getThemeOption(themeId: Theme): ThemeOption | undefined {
    return this.themes.find(t => t.id === themeId);
  }

  private loadSavedTheme() {
    try {
      const saved = localStorage.getItem(this.storageKey) as Theme;
      if (saved && this.themes.find(t => t.id === saved)) {
        this.applyTheme(saved);
      } else {
        this.applyTheme('light');
      }
    } catch (e) {
      this.applyTheme('light');
    }
  }
}
