# Japanese Flashcards

A simple application created to learn japanese words. Contains built-in access to a dictionary, so you don't need to type word/translation manually. You can just find it and immediately add it to your list. Reviewing system is similar to Leitner system, only has more options when answering (miss, easy, normal and hard instead of know / don't know).

## В качестве курсовой и на что обратить внимание

Основной код лежит [тут](https://github.com/KirillEmets/japaneseflashcards/tree/master/app/src/main/java/com/kirillemets/flashcards). Каждая папка - отдельный package, отвечающий за свой фрагмент. Review - основная страница для повторений, addword - словарь и т.д. В папке database всё, что работает с бд. В network - запрос к онлайн словарю.  
Каждый из фрагментов сделан по принципу mvvm. Сам фрагмент это графическая часть и навигация, ViewModel - класс, в котором происходит вся логика.  
Поскольку это андроид приложение, немалая часть асинхронки это ивенты и observable, которые отвечают за нажатия на кнопки и изменения текста на экране. Для остального используються котлиновские корутины (выход в отдельный поток для доступа к бд или сетевой запрос) 
