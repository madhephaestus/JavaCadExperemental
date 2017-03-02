import eu.mihosoft.vrl.v3d.Polygon;
import eu.mihosoft.vrl.v3d.Vertex;
import java.util.ArrayList;

ArrayList<CSG> mink(CSG target, CSG travelingShape){
	println "Polygons :"+target.getPolygons().size()
	ArrayList<CSG> allFaces = new ArrayList<CSG>()
	int counter=0;
	for(Polygon p: target.getPolygons()){
		counter+=1
		println "Count = "+counter+" of "+target.getPolygons().size()
		ArrayList<CSG> corners =new ArrayList<CSG>()
		for(Vertex v:p.vertices){
			corners.add(travelingShape.transformed(new Transform().translate(v.x,v.y,v.z)))
		}
		CSG face = corners.remove(0)
		face=face.hull(corners)
		BowlerStudioController.addCsg(face)
		allFaces.add(face)
	}
	return allFaces
}


CSG cube = new Cube(30).toCSG()
cube=cube.union(new Sphere(10).toCSG().movez(15))
BowlerStudioController.setCsg([cube])

CSG printNozzel = new Cube(0.5).toCSG()
ArrayList<CSG> shell = mink(cube,printNozzel)

CSG orig = cube.movex(41)
BowlerStudioController.addCsg(orig)
println "Performing union ..."
CSG unionShell= shell.remove(0)
unionShell = unionShell.union(shell)
println "Performing difference ..."
CSG cutOut = cube.difference(unionShell)
println "Moving difference ..."
shell.add(orig)
shell.add(cutOut.movex(82))
return shell