# Wymagania Funkcjonalne dla Systemu Biblioteki

## 1. Zarządzanie Książkami
- **RF1.1**: Administrator może dodawać nowe książki do katalogu, podając tytuł, autora, rok wydania i opis.
- **RF1.2**: Administrator może edytować istniejące książki w katalogu.
- **RF1.3**: Administrator może usuwać książki z katalogu.
- **RF1.4**: Użytkownicy mogą przeglądać listę wszystkich książek w katalogu.
- **RF1.5**: System umożliwia wyszukiwanie książek po tytule, autorze lub roku wydania.

## 2. Zarządzanie Użytkownikami
- **RF2.1**: System obsługuje dwa typy użytkowników: administrator i zwykły użytkownik.
- **RF2.2**: Administrator ma dostęp do wszystkich funkcji zarządzania.
- **RF2.3**: Zwykły użytkownik może tylko przeglądać książki i wypożyczać je.

## 3. Autoryzacja i Uwierzytelnianie
- **RF3.1**: Użytkownicy mogą się logować do systemu, wybierając rolę (admin lub user).
- **RF3.2**: System sprawdza rolę użytkownika przed wykonaniem akcji wymagających uprawnień administratora.
- **RF3.3**: Użytkownicy mogą się wylogować z systemu.

## 4. Wypożyczanie Książek
- **RF4.1**: Zalogowany użytkownik może wypożyczyć dostępną książkę.
- **RF4.2**: System śledzi, kto ma wypożyczoną książkę i kiedy została wypożyczona.
- **RF4.3**: Użytkownik może zwrócić wypożyczoną książkę.
- **RF4.4**: Administrator może zobaczyć listę wszystkich wypożyczeń.

## 5. Interfejs Użytkownika
- **RF5.1**: Aplikacja ma responsywny interfejs webowy zbudowany w React.
- **RF5.2**: Główna strona wyświetla listę książek i opcje logowania.
- **RF5.3**: Administrator ma dodatkowy formularz do dodawania książek.
- **RF5.4**: Wszystkie dane są wyświetlane w tabelach lub listach.

## 6. Wymagania Niefunkcjonalne
- **RNF1**: Aplikacja musi być napisana w TypeScript dla lepszej niezawodności.
- **RNF2**: Kod musi być modułowy i łatwy do rozszerzania.
- **RNF3**: System musi obsługiwać co najmniej 1000 książek i 100 użytkowników.

## 7. Modele Danych
- **Book (Items)**: id, tytuł, autor, rok, opis
- **User**: id, imię, nazwisko, email, status (admin/user)
- **Katalog**: zbiór książek, zbiór kopii, nazwa, opis, użytkownik
- **Copy**: id, itemId, status, kto ma książkę

Te wymagania stanowią podstawę dla implementacji funkcjonalności biblioteki.