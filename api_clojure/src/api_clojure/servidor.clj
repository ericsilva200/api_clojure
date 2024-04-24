(ns api-clojure.servidor
  (:require
            [io.pedestal.http :as http]
            [io.pedestal.test :as test]
            [io.pedestal.interceptor :as i]
            [api-clojure.database :as database]
            [api-clojure.rotas :as routes]))


(defn assoc-store [context]
  (update context :request assoc :store database/store))

(def db-interceptor
  {:nome :db-interceptor
   :enter assoc-store})

(def service-map-base {::http/routes routes/routes
                  ::http/port 9999
                  ::http/type :jetty
                  ::http/join? false})

(def service-map (-> service-map-base
                     (http/default-interceptors)
                     (update ::http/interceptors conj (i/interceptor db-interceptor))))
"Por ser um atom o nosso server pode ser redefinido como uma variável, o defonce faz com que nossa variável seja definida como nulo apenas uma vez"
(defonce server (atom nil))

"Função para dar start em nosso servidor"
(defn start-server []
  (reset! server (http/start (http/create-server service-map))))

"Função para testar nossa request no REPL"
(defn test-request [verb url]
  (test/response-for (::http/service-fn @server) verb url))

"parar servidor"
(defn stop-server []
  (http/stop @server))

"restartar o servidor"
(defn restart-server []
  (stop-server)
  (start-server))

(defn start []
  (try (start-server) (catch Exception e (println "Erro ao executar start" (.getMessage e))))
  (try (restart-server) (catch Exception e (println "Erro ao executar restart" (.getMessage e)))))


