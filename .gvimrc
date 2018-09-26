let g:fc_DontUseDefault = 1
if has("macunix")
    set guifont=Monaco:h11
    set columns=156 lines=99
elseif has("unix")
    set guifont=Monaco:h12
else
    set guifont=Monaco:h7.5:cANSI
    set columns=150 lines=99
endif
color white
