#!/usr/bin/python 
from eliminateBackground import eliminateBackground
from time import gmtime, strftime
from threading import *
from socket import *
import json
import struct
import os.path
import cv2

class Treelogy_Agent(Thread):
	def __init__(self, userID, kill_lock, kill_condition, agentInstanceList, identifierSemaphore, identifierQueue):
		print "[" + self.get_time() + "] " + "Agent #" + str(userID) + "is created"
		self.identifierQueue = identifierQueue
		self.identifierSemaphore = identifierSemaphore
		self.agentInstanceList = agentInstanceList
		self.kill_lock = kill_lock
		self.kill_condition = kill_condition
		self.isdestroy = False
		self.instance = None
		self.userID = userID
		self.image = None
		self.connection = None
		self.address = None
		self.run_count = 0
		self.image_size = 0
		#must be modified manually when server is deployed on another machine
		self.save_path = '/home/cuguilke/caffe/examples/images/' 
		self.contribution_path = '/home/cuguilke/contributions/' 
		Thread.__init__(self)

	def run(self):
		#send 1 to the client if connection is successfull
		#when client gets 1, it sends image size
		#when the agent gets the image size, it sends back to user
		#if the image size is correct, user sends "OK" to client
		#after that user immediately starts to send image packets
		try:
			self.agentInstanceList.append({"Agent": self, "ID": self.userID, "instance": None})
			while True:
				self.connection.sendall("1\n") 
				message = self.connection.recv(8)
				buf = ""
				for i in range(0,len(message)):
					if message[i] != "#": #seperator sent from client for accurate int calc
						buf += message[i]
					else:
						break
				if buf == "destroy": #means no more identification is required
					self.isdestroy = True
					self.kill_lock.acquire()
					self.kill_condition.notifyAll()
					self.kill_lock.release()
					break
				elif buf == "donate":
					self.get_contribution()
					break
				self.connection.sendall(buf + "#\n") 
				self.image_size = int(buf)
				line = self.connection.recv(2)
				if line == "OK":
					#get the image and save it with name "image{userID}#{run_count}"
					buf = b''
					while len(buf) < self.image_size:
						data = self.connection.recv(self.image_size - len(buf))
						if not data:
							break
						buf += data
					imagename = "image" + str(self.userID) + "#" + str(self.run_count) + ".jpg"
					completename = os.path.join(self.save_path, imagename)
					fp = open(completename, "wb")
					fp.write(buf)
					fp.close()
					#apply image processing procedures
					img = cv2.imread(completename, 1)
					eliminateBackground(img, completename)
					#acquire & run Treelogy_Identifier
					self.identifierSemaphore.acquire()
					self.get_Treelogy_Identifier()
					for i in self.agentInstanceList:
						if i["ID"] == self.userID:
							#set instance state for controlling purposes
							i["Agent"] = self
							i["instance"] = True
							i["ID"] = self.userID
							break
					self.instance.run(imagename)
					result = self.instance.get_results()
					#identification is done, release Treelogy_Identifier
					self.release_Treelogy_Identifier()
					self.identifierSemaphore.release()
					for i in self.agentInstanceList:
						if i["ID"] == self.userID:
							#set instance state for controlling purposes
							i["instance"] = None
							break
					self.run_count += 1
					#send identification result to client
					result_json = json.dumps(result, sort_keys=True)
					if self.connection:
						frmt = "=%ds" % len(result_json)
						packedMessage = struct.pack(frmt, result_json)
						#packedHeader = struct.pack('=I', len(packedMessage)) 
						#send size of the result
						#self._send(packedHeader) 
						#send the result
						self._send(packedMessage) 
						self._send("\n") 
				else: 
					print  "[" + self.get_time() + "] " + "Client #" + str(self.userID) + ": Data Corruption..."
			self.connection.close()
		except timeout:
			print "[" + self.get_time() + "] " + "Client #" + str(self.userID) + ": Connection timeout..."
			self.isdestroy = True
			self.kill_lock.acquire()
			self.kill_condition.notifyAll()
			self.kill_lock.release()
			self.connection.close()

	def get_instance(self):
		return self.instance

	def _send(self, msg):
		sent = 0
		while sent < len(msg):
			sent += self.connection.send(msg[sent:])

	def set_connection(self, connection, address):
		#MUST BE called before starting the thread
		self.connection = connection
		self.address = address
		print "[" + self.get_time() + "] " + "Client #" + str(self.userID) + " is connected to the server from " + str(self.address)

	def get_Treelogy_Identifier(self):
		self.instance = self.identifierQueue.get()
		print "[" + self.get_time() + "] " + "Client #" + str(self.userID) + " acquired Treelogy_Identifier#" + str(self.instance.ID)

	def release_Treelogy_Identifier(self):
		instance_id = self.instance.ID
		self.identifierQueue.put(self.instance)
		self.instance = None
		print "[" + self.get_time() + "] " + "Client #" + str(self.userID) + " released Treelogy_Identifier#" + str(instance_id)

	def get_time(self):
		return strftime("%a, %d %b %Y %X", gmtime())

	def get_contribution(self): 
		message = self.connection.recv(8)	
		buf = ""
		for i in range(0,len(message)):
			if message[i] != "#": #seperator sent from client for accurate int calc
				buf += message[i]
			else:
				break
		self.connection.sendall(buf + "#\n") 
		line = self.connection.recv(2)
		if line == "OK":
			self.image_size = int(buf)
			#get size of the latin name of the image
			message = self.connection.recv(8)
			buf = ""
			for i in range(0, len(message)):
				if message[i] != "#":
					buf += message[i]
				else:
					break
			namesize = int(buf)
			#get the latin name of the image
			buf = ""
			while len(buf) < namesize:
				data = self.connection.recv(namesize - len(buf))
				if not data:
					break
				buf += data
			species = buf.lower()
			#get the image and save it with name "contribution{userID}#{run_count}"
			buf = b''
			while len(buf) < self.image_size:
				data = self.connection.recv(self.image_size - len(buf))
				if not data:
					break
				buf += data
			imagename = "contribution" + str(self.userID) + "#" + str(self.run_count) + ".jpg"
			target_dir = self.contribution_path + species + '/'
			if not os.path.exists(target_dir):
				os.makedirs(target_dir)
			completename = os.path.join(target_dir, imagename)
			fp = open(completename, "wb")
			fp.write(buf)
			fp.close()
			self.connection.sendall("completed#\n") 
			print "[" + self.get_time() + "] " + "Client #" + str(self.userID) + " contributed an image to tree species named " + species
			self.isdestroy = True
			self.kill_lock.acquire()
			self.kill_condition.notifyAll()
			self.kill_lock.release()
		else: 
			print  "[" + self.get_time() + "] " + "Agent #" + str(self.userID) + ": Data Corruption..."
			
