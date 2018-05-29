
# Lab 3. Słabe/miękkie referencje i wątki.

## Wymagania:
 1. wiedza dotycząca działania i użycia słabych i miękkich referencji. 
 2. Tworzenie i synchronizacja wątków w Java'ie (także narzędzia typu java.util.concurrent).

## Zadanie:

1. 
Należy utworzyć metodę, której argumentem będzie liczba int/long (pełniąca funkcję ziarna) i która generuje kolekcję wartości typu double. Lista powinna być duża – rozmiar powinien zależeć od ziarna i być w zakresie od jednego do kilku megabajtów. Wartości w kolekcji powinny być zależne od ziarna tzn. dwukrotne użycie tego samego ziarna wygeneruje taką samą kolekcję (rozmiar i wartości w kolekcji).

2. 
Nalezy utworzyć pamięć podręczną dla tworzonych kolekcji: kluczem jest wartość ziarna, a wartością kolekcja odpowiadająca temu ziarnu. Należy wykorzystać mechanizm miękkich referencji, w celu zapewnienia, że Garbage Collector w przypadku braku wolnej pamięci usunie część wpisów, by zrobić miejsce na nowy wpis. Jak działanie pamięci podręcznej zmieni się przy użyciu zwykłych (silnych) lub słabych referencji (zamiast miękkich)?

3. 
Należy utworzyć kilka (co najmniej 5) wątków. Każdy wątek periodycznie próbuje obliczyć pewną statystykę dla kolekcji o losowym ziarnie. Statystyki mogą być różnorodne: średnia, wariancja, rozstęp, minimum, maksimum, mediana, liczba elementów dodatnich itp. Wątki pracują na pamięci podręcznej: jeśli wątek ma obliczyć statystykę dla kolekcji nie będącej obecnie w pamięci podręcznej, to musi najpierw tą kolekcję wygenerować (z ziarna) i umieścić ją w pamięci. Dostęp do pamięci podręcznej powinien być kontrolowany (synchronizacja).

4. 
Każdy wątek powinien drukować log swojej działalności (obliczanie statystyki, umieszczanie kolekcji w pamięci podręcznej) do pliku (osobnego dla każdego wątku) oraz na konsolę (wspólną dla wszystkich wątków). Ponadto należy wykorzystać odpowiednie mechanizmy (np. kolejki referencji) by zliczać sytuacje, w których jakaś kolekcja została usunięta z pamięci podręcznej. Pod koniec pracy aplikacji na konsoli powinna się pojawić informacja ile razy nastąpiło usunięcie wpisu z pamięci podręcznej.

5. 
Ograniczyć możliwe wartości ziarna (np. od 0 do kilku tysięcy). W razie potrzeby należy wykorzystać opcje maszyny wirtualnej Java'y służące do kontrolowania rozmiaru sterty (-Xms, -Xmx), w celu ułatwienia obserwacji działania miękkich referencji.


### SoftReference

Klasa java.lang.ref.SoftReference reprezentuje referencję miękką. Referencje te różnią się od referencji silnych tym, że obiekty na które wskazują zostaną usunięte w sytuacji gdy GC stwierdzi niewystarczającą ilość pamięci dla innych obiektów aplikacji. Sprzątnięcie obiektów, na które wskazują referencje miękkie nastąpi jeszcze przed rzuceniem wyjątku OutOfMemoryError, co gwarantuje nam specyfikacja JVM.

Jednym z zastosowań referencji miękkich są mechanizmy cache’owania danych. W implementacji takiego mechanizmu pożądane jest aby dane z cache zostały usunięte jeżeli zacznie brakować pamięci dla pozostałych obiektów. Referencje do obiektów mogą być oczywiście silne i sami możemy zarządzać sytuacjami wyjątkowymi, ale po co jeżeli pewne aspekty może załatwić za nas sam GC. 

### ReferenceQueue
W przypadku gdy SoftReference bądź WeakReference zaczną zwracać null obiekt, na który wskazywały jest oznaczony jako obiekt do finalizacji/usunięcia z pamięci. W takiej sytuacji chcielibyśmy jednak mieć możliwość wykonania pewnych operacji czyszczące związanych z daną referencją, np. w przypadku usunięcia referencji do pewnego klucza z WeakHashMap chcielibyśmy usunąć także wartość związaną z tym kluczem. Z pomocą przychodzą tzw. kolejki referencji, do których zapisywane są referencje do obiektów oznaczonych jako obiekty do finalizacji.

#### Źródło: http://dembol.org/blog/2012/04/07/silne-i-slabe-referencje-w-java/