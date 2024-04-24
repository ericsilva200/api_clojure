(ns api-clojure.database)

"formato: {id {tarefa_id tarefa_nome tarefa_status}"
(def store (atom {}))