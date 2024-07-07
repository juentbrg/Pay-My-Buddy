# Modèle Physique des Données (MPD)

## 1. Table User

### Description
Stocke les informations des utilisateurs.

### Colonnes
- **user_id** : INT, NOT NULL, PRIMARY KEY, AUTO_INCREMENT
- **username** : VARCHAR(100), NOT NULL
- **email** : VARCHAR(255), NOT NULL
- **password** : VARCHAR(255), NOT NULL

---

## 2. Table UserConnection

### Description
Stocke les connexions entre utilisateurs.

### Colonnes
- **connection_id** : INT, NOT NULL, PRIMARY KEY, AUTO_INCREMENT
- **user1_id** : INT, NOT NULL
- **user2_id** : INT, NOT NULL

### Contraintes
- Clé étrangère (**user1_id**) référencée à **User(user_id)**, ON DELETE CASCADE
- Clé étrangère (**user2_id**) référencée à **User(user_id)**, ON DELETE CASCADE

---

## 3. Table Transaction

### Description
Stocke les transactions entre utilisateurs.

### Colonnes
- **transaction_id** : INT, NOT NULL, PRIMARY KEY, AUTO_INCREMENT
- **sender_id** : INT, NOT NULL
- **receiver_id** : INT, NOT NULL
- **description** : VARCHAR(255)
- **amount** : DECIMAL(16, 2), NOT NULL

### Contraintes
- Clé étrangère (**sender_id**) référencée à **User(user_id)**, ON DELETE CASCADE
- Clé étrangère (**receiver_id**) référencée à **User(user_id)**, ON DELETE CASCADE

