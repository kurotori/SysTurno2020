SELECT Receta.id as Receta,Contiene.medicamento_id, Contiene.entregado,
	(SELECT Medicamento.nombre FROM Medicamento WHERE Medicamento.id = Contiene.medicamento_id) as "Nombre del Medicamento",
    (SELECT Recibe.usuario_ci FROM Recibe WHERE Recibe.receta_id = Receta.id) as "CI",
    (SELECT CONCAT_WS(" ",Perfil.nombre,Perfil.apellido) FROM Perfil WHERE Perfil.id = (SELECT Recibe.usuario_ci FROM Recibe WHERE Recibe.receta_id = Receta.id)) as "Nombre"
FROM Receta INNER JOIN Contiene
WHERE Receta.id=Contiene.receta_id;