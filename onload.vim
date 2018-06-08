" Vim project configuration script

if has('win32')
    compiler msvc
elseif has('win32unix')
    compiler javac
endif
set fdm=marker

" Do this only once.
if exists('g:did_jguime')
    finish
endif
let g:did_jguime = 1

" ApplyTemplate variables
let g:atpl_UsersList["@AUTHORMAIL@"] = '<antonello.ale@gmail.com>'
let g:atpl_UsersList["@PROJECTNAME@"] = 'jguime'
let g:atpl_UsersList["@PROJECT@"] = 'jguime'
let g:atpl_UsersList["@PACKAGE@"] = 'x.android'
let g:atpl_UsersList["@OWNER@"]   = 'Alessandro Antonello'
let g:atpl_UsersList["@VERSION@"] = '2.5'

" Special highlighting configuration
let java_highlight_java_lang_ids = 1
let java_ignore_javadoc = 1

" Add an autocommand to set properties about source files
augroup JGUIME
    au BufNewFile  *.java set ff=unix fenc=utf8 syntax=java.doxygen nobomb
    au BufNewFile,BufRead changes setl tw=72
augroup END
