package bilgi.sesliplayer;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.faraji.environment3.Device;
import com.faraji.environment3.Environment3;

public class MuzikTara {
	
	List<String> dirs = new ArrayList<String>();
	List<List<String>> files = new ArrayList<List<String>>();
	
	MuzikTara(){


		List<String> rootPaths = new ArrayList<String>();

		rootPaths.add(Environment.getExternalStorageDirectory().getAbsolutePath());

		for(String rootDir : getStorageDirectories()){
			rootPaths.add(rootDir);
		}
		/*Device[] devices = Environment3.getDevices(null, true, true, false);

		for(Device device : devices){
			rootPaths.add(device.getFile().getPath());
		}*/



		/*try{
			String sdCardPath = new StorageUtil().getRemovebleSDCardPath();
			rootPaths.add(sdCardPath);
			Log.e("buldum","Bunu buldum"+sdCardPath);
			//Toast.makeText(MainActivity.mainActivity, sdCardPath, Toast.LENGTH_LONG).show();

		}catch (Exception e){
			e.printStackTrace();
		}*/
		
		for(String externalPath : rootPaths){
			System.out.println("Kanynak: "+externalPath);
			File f = new File(externalPath);
			walk(f);
		}
		
		if(dirs.size() > 0){
    		if(files.get(files.size()-1).size() == 0)
        	{
        		dirs.remove(dirs.size()-1);
        		files.remove(files.size()-1);
        	}
    	}
		
		for(int i=0; i<dirs.size(); i++){
			System.out.println("yolu: "+dirs.get(i));

			for(String fileName : files.get(i)){
				System.out.println("dosya: "+fileName);
			}
		}
	}	
	
	public List<String> getDirs(){
		return dirs;
	}
	
	public List<List<String>> getFiles(){
		return files ;
	}
	
	public void walk(File root) {

        File[] list = root.listFiles();
		//Log.e("files",root.getAbsolutePath()+" | "+list.length);
		if(list != null) {
			for (File f : list) {
				if (f.isDirectory()) {
					//System.out.println("Dir: " + f.getAbsoluteFile());

					walk(f);
				} else {

					String filename = f.getName();
					String filenameArray[] = filename.split("\\.");
					String extension = filenameArray[filenameArray.length - 1];

					if (extension.toLowerCase().equals("mp3")) {
						System.out.println("Parent: " + f.getParent());
						System.out.println("File: " + f.getAbsoluteFile());

						if (dirs.indexOf(f.getParent()) != -1) {
							files.get(dirs.indexOf(f.getParent())).add(f.getName());
						} else {
							dirs.add(f.getParent());
							files.add(new ArrayList<String>());
							files.get(files.size() - 1).add(f.getName());
						}
					}
				}
			}
		}
	}

	public static String[] getStorageDirectories() {
		String [] storageDirectories;
		String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			List<String> results = new ArrayList<String>();
			File[] externalDirs = MainActivity.mainActivity.getExternalFilesDirs(null);
			for (File file : externalDirs) {
				String path = file.getPath().split("/Android")[0];
				if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Environment.isExternalStorageRemovable(file))
						|| rawSecondaryStoragesStr != null && rawSecondaryStoragesStr.contains(path)){
					results.add(path);
				}
			}
			storageDirectories = results.toArray(new String[0]);
		}else{
			final Set<String> rv = new HashSet<String>();

			if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
				final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
				Collections.addAll(rv, rawSecondaryStorages);
			}
			storageDirectories = rv.toArray(new String[rv.size()]);
		}
		return storageDirectories;
	}
	
}
