SELECT u.user_id, td.training_id, u.username ,td.training_date, COUNT(*) AS count
FROM User u
    JOIN Training_details td ON u.user_id = td.user_id
GROUP BY u.user_id, td.training_id, td.training_date, u.username
HAVING COUNT(*) > 1
ORDER BY td.training_date DESC;

