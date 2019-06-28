" Syntastic Configuration Script for Java

" Java syntax configuration
let java_highlight_java_lang_ids=1
let java_highlight_all=1

let project_root = getcwd()
let project_home = project_root
let android_home  = project_home

let g:JavaComplete_LibsPath = expand('$ANDROID_HOME').'/platforms/android-19/android.jar'

let g:JavaComplete_SourcesPath = android_home.'src'

" Comments below added to use YCM instead of Syntastic
let g:syntastic_java_javac_classpath = g:JavaComplete_LibsPath .':'.android_home.'src/'

" call javacomplete#AddSourcePath(android_home.'src/')
" call javacomplete#SetSearchdeclMethod(4)

augroup JAVARC
    au!
    au BufNewFile,BufReadPre *.java exec "setl syntax=java.doxygen | call mylib#setSyntaxFoldOrNot()"
    au BufWinEnter *.java exec 'setl omnifunc=javacomplete#Complete'
augroup END

" Disable syntastic checkers:
" let g:syntastic_java_checkers = []

unlet project_root
unlet project_home
unlet android_home

