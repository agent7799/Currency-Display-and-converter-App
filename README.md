# Currency-Display-and-converter-App

Android application to display list of currencies and rates fron Central Bank site

Updated 23.03.2022

Андроид приложение со следующими функциями:

1. Загружать список валют с сайта ЦБ https://www.cbr-xml-daily.ru/daily_json.js и отображать
его в виде списка. 
- Раелизовано. Для парсинга JSON использована библиотека gson (Google) как самая распространенная и поддерживаемая. 

2. Предоставлять возможность конвертировать указанную сумму в рублях в выбранную
валюту. 
- Реализовано по нажатию на элемент списка валют.

3. Сохранять данные о курсах валют и не перезагружать их при повороте экрана
- Реализовано
     или перезапуске приложения.
   Не реализовано на момент апдейта
   
Добавить возможность перезагрузить список курсов вручную. 
- Реализовано 

4. Периодически обновлять курсы валют.
- Реализовано. Раз в 15 минут.
