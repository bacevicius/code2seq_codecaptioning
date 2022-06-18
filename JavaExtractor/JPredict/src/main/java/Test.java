/**
load a kml file into google earth application
*/
public void loadKML(File fin) {
    if (googleEarth == null) {
        return;
    }
    try {
        //ICameraInfoGE cam = googleEarth.GetCamera(1);
        googleEarth.OpenKmlFile(fin.getAbsolutePath(), 0);
        //googleEarth.SetCamera(cam, 100);
        System.out.println(fin.getAbsolutePath());
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}