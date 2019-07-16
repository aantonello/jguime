let g:fc_DontUseDefault = 1
if has("macunix")
    set guifont=Monaco:h10
    set columns=186 lines=99
elseif has("unix")
    set guifont=Monaco:h12
else
    set guifont=Monaco:h7.5:cANSI
    set columns=150 lines=99
endif
color white

let g:NERDTreeWinSize=46
let g:NERDTreeDirArrowExpandable='+'
let g:NERDTreeDirArrowCollapsible='-'

set diffopt=filler,iwhite

" ApplyTemplate Plugin Configuration for project: Mobile Banking

let g:atpl_UsersList["@AUTHORMAIL@"] = '<antonello.ale@gmail.com>'
let g:atpl_UsersList["@PROJECT@"]    = 'jguime'
let g:atpl_UsersList["@OWNER@"]      = 'Alessandro Antonello'
let g:atpl_UsersList["@VERSION@"]    = '2.5'

" Syntastic Configuration Script for Java

" let project_root = getcwd()
" let project_home = project_root
" let android_home = project_home

" call javacomplete#AddClassPath(expand('$ANDROID_HOME').'/platforms/android-19/android.jar')
" call javacomplete#AddSourcePath(android_home.'/src/')
" call javacomplete#SetSearchdeclMethod(4)

augroup JGUIME
  au! FileType java call <SID>setupFile()
augroup END

function s:setupFile()
  setlocal omnifunc=javacomplete#Complete
  setlocal completefunc=javacomplete#CompleteParamsInfo
endfunction

" unlet project_root
" unlet project_home
" unlet android_home
