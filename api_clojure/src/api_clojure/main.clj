(ns api-clojure.main
  (:require [api-clojure.servidor :as servidor]
            ))

(servidor/start)

"Realiza uma requisição através do nosso método de teste pelo REPL"
(println (servidor/test-request :get "/hello?name=Eric"))
(println (servidor/test-request :post "/tarefa?nome=Correr&status=pendente"))
(println (servidor/test-request  :post "/tarefa?nome=Correr&status=feito"))
(println (servidor/test-request  :get "/tarefa"))
(println (servidor/test-request  :delete "/tarefa/7aa3a79b-955a-4cf2-aaf6-911a675fd6d0"))
(println (servidor/test-request  :patch "/tarefa/818e6b0e-3ba0-4d85-9aaf-a5ee4d2bfc9a?nome=corrida&status=concluida"))