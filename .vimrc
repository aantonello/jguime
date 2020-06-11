" Projeto configuration file.
" This template enables you to set configurations before any other vim stuff
" is loaded.
" For GUI configurations, use the 'script.gvimrc' template.

" NOTE: to remember:
" let g:ale_plugin_enabled = 1                 " Enable ALE plugin
" let g:android_plugin_enabled = 1             " Enable Android plugin
" let g:ycm_plugin_enabled = 1                 " Enable YouCompleteMe plugin
let g:syntastic_plugin_enabled = 1           " Enable Syntastic plugin
let g:devicons_enabled = v:true              " Enable WebDevIcons.
let g:airline_powerline_fonts = 1            " Enable powerline fonts in airline
let g:loaded_dbext = 1                        " Disable DBExt plugin (not in pack directory)

let g:color_light = 'white'                   " Sets color for day light.
let g:color_night = 'no_quarter'              " Sets color for night.

let g:NERDTreeWinSize=46
let g:NERDTreeMinimalUI=1
let g:NERDTreeDirArrowExpandable = ''
let g:NERDTreeDirArrowCollapsible = ''

" Keep this line. It will ensure that Vim will start smothly.
source $HOME/.vim/vimrc

let g:fc_DontUseDefault = 1
set linespace=1
set guifont=HackNerdFontComplete-Regular:h10
set diffopt=filler,iwhite

let java_ignore_javadoc = 1

let g:atpl_UsersList["@PROJECT@"] = 'jguime'
let g:atpl_UsersList["@OWNER@"]   = 'Alessandro Antonello'
let g:atpl_UsersList["@VERSION@"] = '2.5'

augroup project_init
  au!
  au GUIEnter * set columns=190 lines=99
  au BufEnter * call mylib#manageSigns('java')
augroup END


" vim:fdm=marker:fmr={{{,}}}:ts=2:sw=2:
