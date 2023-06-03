# Billennium

Sklad zespolu : 

Piotr Kepisty 
- email : piotrkepistyuwm@gmail.com
- rola : Backend

Arkadiusz Ugniewski
- email: arek.ugn@wp.pl
- rola : frontend

Bartosz Biedrzycki 
- email: bartosz.b2468@gmail.com
- rola: backend

Technologie:
- Java (backend)
- Angular (frontend)

Uruchomienie aplikacji 

  Wymagane programy:
    - Intellij (środowisko Java)
    - Dbeaver (program do zarzadzania bazą danych, mogą być inne(postgresql))
    - Pgadmin4 (do utworzenia bazy danych)
    - Postman (do obsługi zapytań HTTP)
    

1. Tworzymy bazę danych w pgAdmin4.
2. Tworzymy połączenie z bazą danych np w DBeaver.
3. Wprowadzamy dane połączenia z bazą danych w pliku .yml.
4. Uruchamiamy aplikację.
5. Baza danych zostanie wygenerowana automatycznie po uruchomieniu aplikacji jeśli mamy skonfugurowane połączenie.
6. Po uruchomieniu aplikacji zostanie wygenerowany token
7. Uruchamiamy postmana
8. Wprowadzamy doken do Auth w postmanie aby żądanie mogło byc prawidłowo obsłużone
9. Wprowadzamy żądane parametry dla danego endpointu w kontrolerze.
