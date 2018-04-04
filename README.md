# FrenchNumbersApp


Just a simple Android app to learn French numbers (it isn't a simple task, so..))

At first, a random number is generating. App tries to show some numbers while generating but there are a lot of issues with it in various phones.


Then, user can press "speak" , the app uses Google API to recognize user's speech and tries to extract a text from it. 
There are some french numbers (0-123) hardcoded in the app as numberal constants so it is trivial to find the matching number. 

If the text is a given number, user are moving to a next one. 

User can listen the French number (using Text-to-Speech engine).

There are different colors for labels : green - ok, user said the right one; yellow - good, user said the good one after viewing the hint or listening 
to a text-to-speech; red - user changed the number without actually prononced it. 





So the app is just for a french fun.
