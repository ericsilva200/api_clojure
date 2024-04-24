
(ns api-clojure.rotas
  (:require
    [io.pedestal.http.route :as route]
    ))



(defn listar-tarefas [request]
  {:status 200 :body @(:store request)})

(defn criar-tarefa-mapa [uuid nome status]
  {:id uuid :nome nome :status status})

(defn criar-tarefa [request]
  (let [uuid (java.util.UUID/randomUUID)
        nome (get-in request [:query-params :nome])
        status (get-in request [ :query-params :status])
        tarefa (criar-tarefa-mapa uuid nome status)
        store (:store request)]
    (swap! store assoc uuid tarefa)
    {:status 200 :body {:mensagem "Tarefa concluída com sucesso!"
                        :tarefa tarefa}}))

(defn funcao_hello [request]
  {:status 200 :body (str "Bem Vindo! " (get-in request [:query-params :name] "Everybody")) })

(defn delete-tarefa [request]
  (let [store (:store request)
        tarefa-id (get-in request [:path-params :id])
        tarefa-id-uuid (java.util.UUID/fromString tarefa-id)]
    (swap! store dissoc tarefa-id-uuid)
    {:status 200 :body {:mensagem "Removida com sucesso"}}))

(defn atualiza-tarefa [request]
  (let [
        tarefa-id (get-in request [:path-params :id])
        tarefa-id-uuid (java.util.UUID/fromString tarefa-id)
        nome (get-in request [:query-params :nome])
        status (get-in request [:query-params :status])
        tarefa (criar-tarefa-mapa tarefa-id-uuid nome status)
        store (:store request)]
    (swap! store assoc tarefa-id-uuid tarefa)
    {:status 200 :body {:mensagem "Tarefa atualizada com sucesso"
                        :tarefa tarefa}}))

(def routes (route/expand-routes
              #{["/hello" :get funcao_hello :route-name :hello-world]
                ["/tarefa" :post  criar-tarefa :route-name :criar-tarefa]
                ["/tarefa" :get  listar-tarefas :route-name :lista-tarefas]
                ["/tarefa/:id" :delete delete-tarefa :route-name :delete-tarefa]
                ["/tarefa/:id" :patch atualiza-tarefa :route-name :atualiza-tarefa]}))