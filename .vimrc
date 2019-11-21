" Projeto configuration file.
" This template enables you to set configurations before any other vim stuff
" is loaded.
" For GUI configurations, use the 'script.gvimrc' template.

" NOTE: to remember:
" let g:ale_plugin_enabled = 1                 " Enable ALE plugin
" let g:android_plugin_enabled = 1             " Enable Android plugin
let g:ycm_plugin_enabled = 1                 " Enable YouCompleteMe plugin
" let g:syntastic_plugin_enabled = 1           " Enable Syntastic plugin
let g:loaded_dbext = 1                        " Disable DBExt plugin (not in pack directory)

" Keep this line. It will ensure that Vim will start smothly.
source $HOME/.vim/vimrc

let g:fc_DontUseDefault = 1
if has("macunix")
  set guifont=Monaco:h10
elseif has("unix")
  set guifont=Monaco:h12
else
  set guifont=Monaco:h7.5:cANSI
endif
" color white

let g:NERDTreeWinSize=46
let g:NERDTreeDirArrowExpandable='+'
let g:NERDTreeDirArrowCollapsible='-'

set diffopt=filler,iwhite

let g:atpl_UsersList["@AUTHORMAIL@"] = '<antonello.ale@gmail.com>'
let g:atpl_UsersList["@PROJECT@"]    = 'jguime'
let g:atpl_UsersList["@OWNER@"]      = 'Alessandro Antonello'
let g:atpl_UsersList["@VERSION@"]    = '2.5'

" vim:fdm=marker:fmr={{{,}}}:ts=2:sw=2:
