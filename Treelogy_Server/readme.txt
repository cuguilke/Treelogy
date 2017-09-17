Date: 04.11.2016 
Author: Ilke Cugu

-#INSTRUCTIONS FOR PROPER CAFFE TREE IDENTIFICATION SERVER SETUP#-

!!!This document directly assumes that you downloaded all files of 
the server.
File list:
-Treelogy_Server.py
-Treelogy_Agent.py
-Treelogy.py
-operations.py
-eliminateBackground.py
-TestClient.java

___________________________________________________________________


%%%%%%%%%%%%%%%%%%%%%%% Treelogy_Server.py %%%%%%%%%%%%%%%%%%%%%%%

1) 'HOST' & 'PORT' variables must be modified manually
2) Thread Pool size can be modified by doing:
	a) In 'reload_agent_pool()' func change range(0, x)'s x to the
	size you want.
	b) The same x must be changed in 'agent_pool_population == x'
	of the main while loop which is located at line 101.(Line 
	numbers may be changed, but this document may not. Therefore,
	may it be easy.) 
3) One can change the total number of identification units which
	run concurrently by doing:
	a) Change the range of the 'for' loop at line 82.
	b) Change the number of semaphores created at line 84.
4) That's it. Have a nice day, or don't.  		

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

___________________________________________________________________


%%%%%%%%%%%%%%%%%%%%%%% Treelogy_Agent.py  %%%%%%%%%%%%%%%%%%%%%%%

1) You must have OpenCV installed at your device.
2) 'self.save_path' variable must be modified manually. You have to
point that variable to your '.../caffe/examples/images/'. Why?
Because, I'm lazy and I use default location to feed images to 
Caffe.
3) 'self.contribution_path' variable must be modified manually.
4) Saved imgname method can be modified. Current method is to save
the image with format "image{userID}#{run_count}". In order to try
something else, you have to modify 'imagename' variable.
5) If you are curious about what kind of workflow I use within the
server. Read the comments section of 'run()' function.
6) I will say no more about this file.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

___________________________________________________________________


%%%%%%%%%%%%%%%%%%%%% Treelogy_Identifier.py %%%%%%%%%%%%%%%%%%%%%%

1) If you were able to install Caffe itself, this will be a piece 
of cake.
2) 'caffe_root' variable must be modified manually.
3) If you do not have CUDA supported Caffe:
	- Comment out lines 16 & 17 
	- Uncomment line 14 ( caffe.set_mode_cpu() )
4) If you do not have the latest 'tree_identification_vx.caffemodel'
at your '...caffe/models/finetune_flickr_style/', you will get an
excellent error message indicating that you should have.
5) You have to have the latest 'synsetwords.txt' at your '...caffe/
data/flickr_style/' and 'deploy.prototxt' at your '...caffe/models/
finetune_flickr_style/'.
6) You can always change the format of resulting Python dictionary
by modifying 'guess' variable. Go to lines 72 & 73 for that. But,
of course you have to modify 'TestClient.java' too in this case.
You should focus on JSON parsing operations part for the necessary
adjustments.
7) For SVM_Tree_Identifier to work properly:
	a) One must have a directory named 'SVM' under 'caffe_root'
	b) 3 files are necessary under 'svm_root':
		I) svmClassifier_Pickle_caffe_finetune_fc6_v9.p
		II) synset_words_Pickle_caffe_finetune_fc6_v9.p
		III) svmClassifier_Pickle_caffe_finetune_fc6_v9.p_01.npy.z
	c) scikit-learn library required.
8) Enough about 'Treelogy_Identifier.py'.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

___________________________________________________________________


%%%%%%%%%%%%%%%%%%%%%%%%% operations.py  %%%%%%%%%%%%%%%%%%%%%%%%%%

1) You must have OpenCV installed at your device.
2) You must have the Python Imaging Library (PIL).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

___________________________________________________________________


%%%%%%%%%%%%%%%%%%%%% eliminateBackground.py  %%%%%%%%%%%%%%%%%%%%%

1) You must have OpenCV installed at your device.
2) If you have an OpenCV lower than v3.0.0, you have to change the 
arguments of the function 'cv2.kmeans()' at line 33:
	- Change '(ab_list,nColors,None,criteria,10,flags)' to
	- '(ab_list,nColors,criteria,10,flags)'
3) If any problem other than the above one occurs, just improvise.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

___________________________________________________________________


%%%%%%%%%%%%%%%%%%%%%%%% TestClient.java  %%%%%%%%%%%%%%%%%%%%%%%%%

1) IP & port for Socket construction at line 22 must be modified 
manually.
2) If you do not have the JSON.jar I have, just comment out lines
7-9 & 54-59. Then uncomment line 53 in order to see the results
returned from the server.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

___________________________________________________________________


!!!You reached here, congratulations! Now all you have to do is to
type "python Treelogy_Server.py" at 1 terminal, and type "java 
TestClient {imagename}.jpeg" at another 1.
- If everything goes well, you will have an elegant leaf based tree
identification system.
- Else, you probably waste your time for nothing. Sorry...  
