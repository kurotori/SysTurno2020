SELECT 
	Receta.id as Receta,
	Contiene.medicamento_id as MedID, 
	Contiene.entregado as "Entregado?",
	(SELECT Medicamento.nombre FROM Medicamento 
		WHERE Medicamento.id = Contiene.medicamento_id) 
		as "Nombre del Medicamento",
    (SELECT Recibe.usuario_ci FROM Recibe 
		WHERE Recibe.receta_id = Receta.id) 
		as "CI",
    (SELECT CONCAT_WS(" ",Perfil.nombre,Perfil.apellido) FROM Perfil 
		WHERE Perfil.id = 
		(SELECT Recibe.usuario_ci FROM Recibe 
		WHERE Recibe.receta_id = Receta.id))
		as "Usuario",
	(SELECT CONCAT_WS(" ",Doctor.nombre,Doctor.apellido) From Doctor
		WHERE Doctor.ci = 
		(SELECT Entrega.doctor_ci FROM Entrega
		WHERE Entrega.receta_id = Receta.id))
		as "Especialista"
FROM Contiene INNER JOIN Receta INNER JOIN Recibe
WHERE Receta.id=Contiene.receta_id AND
Receta.id = Recibe.receta_id AND
Recibe.usuario_ci = : AND
Contiene.entregado = "no"
;


^~`zxzxzx{}{}]]{{}}}