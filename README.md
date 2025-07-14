# 💰 PatFin - Personal Finance Manager

**Nowoczesna aplikacja do zarządzania finansami osobistymi**

![PatFin Logo](https://img.shields.io/badge/PatFin-Finance%20Manager-green?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEyIDJMMTMuMDkgOC4yNkwyMCA5TDEzLjA5IDE1Ljc0TDEyIDIyTDEwLjkxIDE1Ljc0TDQgOUwxMC45MSA4LjI2TDEyIDJaIiBmaWxsPSJ3aGl0ZSIvPgo8L3N2Zz4K)
[![Angular](https://img.shields.io/badge/Angular-18-DD0031?style=flat&logo=angular)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178C6?style=flat&logo=typescript)](https://www.typescriptlang.org/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk)](https://openjdk.org/)

## 🚀 Demo

🌐 **[Obejrzyj live demo](https://patfin-demo.vercel.app)** *(jeśli dostępne)*

## 📖 Opis

PatFin to kompletna aplikacja full-stack do zarządzania finansami osobistymi, która pomaga użytkownikom śledzić wydatki, analizować przychody i podejmować mądre decyzje finansowe. Aplikacja oferuje intuicyjny interfejs, zaawansowane statystyki oraz bezpieczne przechowywanie danych.

## ✨ Funkcje

### 🔐 **Autentykacja i Bezpieczeństwo**
- Rejestracja i logowanie użytkowników
- JWT tokeny dla bezpiecznej autoryzacji
- Hashowanie haseł z BCrypt
- Obsługa ról użytkowników (User/Admin)

### 💳 **Zarządzanie Transakcjami**
- Dodawanie przychodów i wydatków
- Kategoryzacja transakcji
- Zaawansowane filtry (data, kwota, kategoria, typ)
- Sortowanie i wyszukiwanie
- Edycja i usuwanie transakcji

### 📊 **Statystyki i Raporty**
- Interaktywne wykresy (Chart.js)
- Wykres kołowy wydatków według kategorii
- Wykres słupkowy miesięcznych statystyk
- Filtry czasowe (1 mies., 3 mies., 6 mies., 1 rok, własny zakres)
- Podsumowanie finansowe w czasie rzeczywistym

## 🛠️ Stack Technologiczny

### Frontend
- **Angular 18** - Framework frontend
- **TypeScript** - Język programowania
- **SCSS** - Style i CSS
- **Chart.js** - Wykresy i wizualizacje
- **Angular Material** - Komponenty UI
- **RxJS** - Reactive programming

### Backend
- **Spring Boot 3.x** - Framework backend
- **Java 17** - Język programowania
- **Spring Security** - Bezpieczeństwo i autoryzacja
- **Spring Data JPA** - ORM i dostęp do danych
- **PostgreSQL** - Baza danych
- **JWT** - JSON Web Tokens
- **Maven** - Zarządzanie zależnościami

### Narzędzia i DevOps
- **Git** - Kontrola wersji
- **npm** - Menedżer pakietów
- **Angular CLI** - Narzędzia developerskie
- **Spring Boot DevTools** - Hot reload

## 📦 Instalacja i Uruchomienie

### Wymagania
- **Node.js** 18+ 
- **Java** 17+
- **PostgreSQL** 13+
- **Maven** 3.6+

### 1. Klonowanie repozytorium
```bash
git clone https://github.com/Budzi08/PatFin_budgetApp.git
cd PatFin_budgetApp
```

### 2. Konfiguracja bazy danych
Utwórz bazę danych PostgreSQL:
```sql
CREATE DATABASE patfin_db;
```

### 3. Konfiguracja backendu
```bash
# Przejdź do katalogu backend
cd .

# Skonfiguruj application.properties
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edytuj application.properties z własnymi danymi bazy danych

# Zainstaluj zależności i uruchom
mvn clean install
mvn spring-boot:run
```

### 4. Konfiguracja frontendu
```bash
# Przejdź do katalogu frontend
cd patfin-frontend

# Zainstaluj zależności
npm install

# Uruchom serwer developerski
ng serve
```

### 5. Uruchomienie aplikacji
- **Backend**: http://localhost:8080
- **Frontend**: http://localhost:4200

## 🗄️ Struktura Bazy Danych

```sql
-- Główne tabele
users (id, email, password, is_admin, created_at)
categories (id, name)
transactions (id, amount, description, date, type, user_id, category_id)

-- Relacje
users 1:N transactions
categories 1:N transactions
```

## 📱 Zrzuty Ekranu

### Strona Główna

### Dashboard Transakcji

### Statystyki

