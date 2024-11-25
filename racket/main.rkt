#lang racket

(module+ main
  (require racket/gui/base)
  (define my-frame% (class frame% ; TODO - необязательный аргумент parent
                      (init my-label my-parent my-width my-height)
                      (if (null? my-parent)
                          (super-new
                           (label my-label)
                           (min-width my-width)
                           (min-height my-height)
                           (stretchable-width false)
                           (stretchable-height false))
                          (super-new
                           (label my-label)
                           (parent my-parent)
                           (min-width my-width)
                           (min-height my-height)
                           (stretchable-width false)
                           (stretchable-height false)))))
  
  (define frame
    (new my-frame%
         [my-label "dnc"]
         [my-parent '()]
         [my-width 400]
         [my-height 170]))
  (define menu-bar
    (new menu-bar%
         [parent frame]))
  (define menu
    (new menu%
         [parent menu-bar]
         [label "Меню"]))
  (new menu-item%
       [parent menu]
       [label "О программе"]
       [callback (lambda (_1 _2) (send (new my-frame% ; TODO - фрейм создается, но все падает
                                            ; TODO - проверка закрыто ли окно
                                      [my-label "О программе"]
                                      [my-parent frame]
                                      [my-width 100]
                                      [my-height 100]) show #t))])
  (new menu-item%
       [parent menu]
       [label "Об авторе"]
       [callback (lambda (_1 _2) (printf "hello\n"))])
  (new menu-item%
       [parent menu]
       [label ""]
       [callback (lambda (_1 _2) (printf "hello\n"))])
  (send frame show #t))
