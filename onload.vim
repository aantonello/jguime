" Vim project configuration script

if has('win32')
    compiler msvc
endif

" Do this only once.
if exists('g:did_AndroidX')
    finish
endif
let g:did_AndroidX = 1

" ApplyTemplate variables
let g:atpl_UsersList["@AUTHORMAIL@"] = '<aantonello@paralaxe.com.br>'
let g:atpl_UsersList["@PROJECTNAME@"] = 'jguime'
let g:atpl_UsersList["@PROJECT@"] = 'jguime'
let g:atpl_UsersList["@PACKAGE@"] = 'x.android'
let g:atpl_UsersList["@OWNER@"]   = 'Paralaxe Tecnologia'
let g:atpl_UsersList["@VERSION@"] = '2.4'

" Special highlighting configuration
let java_highlight_java_lang_ids = 1
let java_ignore_javadoc = 1

" javacomplete configuration
if has('win32')
    call javacomplete#AddClassPath(expand('$ANDROID_HOME').'/platforms/android-17/android.jar')
    call javacomplete#AddSourcePath(getcwd().'/src/x/android')
    call javacomplete#SetSearchdeclMethod(4)
else
    call javacomplete#AddClassPath(expand("$HOME").'/Applications/adt-bundle-mac/sdk/platforms/android-15/android.jar')
    call javacomplete#AddSourcePath(getcwd().'/src/x/android')
    call javacomplete#SetSearchdeclMethod(4)
endif

" Add an autocommand to set properties about source files
augroup JGUIME
    au BufNewFile  *.java set ff=unix fenc=utf8 syntax=java.doxygen nobomb omnifunc=javacomplete#Complete
    au BufReadPost *.java set omnifunc=javacomplete#Complete
augroup END

