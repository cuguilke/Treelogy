#!/usr/bin/python
from Treelogy_Agent import *
from Treelogy_Identifier import Treelogy_Identifier
from socket import *
from Queue import *
import signal, sys, time

#1st Google Compute Engine Ip = 104.154.18.233
#2nd Google Compute Engine Ip = 104.197.247.77
#1st Amazon EC2 Instance Ip = 54.213.7.195
HOST = '192.168.0.10'                 
PORT = 50009   

sock = socket(AF_INET, SOCK_STREAM)  
# for reusing socket several times in short time 
sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)

sock.bind((HOST, PORT))
sock.listen(1)
agentList = []
agentID = 0 #used for agent creation
id_iterator = 0 #used for appropriate agent assignment

class AgentKill(Thread):
	def __init__(self, agentInstanceList, lock, condition):
		Thread.__init__(self)
		self.agentInstanceList = agentInstanceList
		self.lock = lock
		self.condition = condition
		self.serverclosed = False

	def run(self):
		while True:
			self.lock.acquire()
			self.condition.wait()
			if (self.serverclosed == True or self.agentInstanceList.__len__() == 0):
				self.lock.release()
				break
			# kill the thread
			for i in self.agentInstanceList:
				if ( i["Agent"] != None ):
					# destroy it
					if ( i["Agent"].isdestroy == True ):
						i["Agent"].join()
						i["Agent"] = None
						print "Agent #" + str(i["ID"]) + " is destroyed" 
						break

			#print_agentList(self.agentInstanceList)
			self.lock.release()

agentInstanceList = []
agent_pool_population = 0
kill_lock = Lock()
kill_condition = Condition(kill_lock)
killAgent = AgentKill(agentInstanceList, kill_lock, kill_condition)
killAgent.start()

def print_agentList(agentInstanceList):
	print "\n-#Client - Instance List#-"
	for i in agentInstanceList:
		print "CLient - ID = " + str(i["ID"]) + " // ", 
		print "Instance state = " + str(i["instance"] != None)

def reload_agent_pool():
	global agentList, agentID, agentInstanceList, kill_lock, kill_condition
	for i in range(0,100):
		agent_nominee = Treelogy_Agent(agentID, kill_lock, kill_condition, agentInstanceList, identifier_semaphore, identifier_queue)
		# add agent and its connection to a list
		agentList.append({"ID" : agentID, "Agent" : agent_nominee, "Connection" : None})
		# increment agentIDs
		agentID += 1

def signal_term_handler(signal, frame):
	print '-#SIGTERM#-'
	sock.close()
	print ">>Socket is closed"
	sys.exit(0)
 
signal.signal(signal.SIGTERM, signal_term_handler)
identifierList = []
for i in range(0,3):
	identifierList.append(Treelogy_Identifier(i))
identifier_semaphore = Semaphore(3)
identifier_queue = Queue()
for i in identifierList:
	identifier_queue.put(i)
reload_agent_pool()
print ">>Waiting for connections"
while True:
	try:
		# accept users
		connection, address = sock.accept()
		# give an agent to user
		agent = agentList[id_iterator]["Agent"]
		connection.settimeout(7.0)
		agentList[id_iterator]["Connection"] = connection
		agent.set_connection(connection, address)
		id_iterator += 1
		agent_pool_population += 1
		if agent_pool_population == 100:
			agent_pool_population = 0
			reload_agent_pool()
		#agent = Treelog_Agent
		agent.start()

	except KeyboardInterrupt:
		print "Keyboard Interrupt"
		sock.close()
		print ">>Socket is closed"
		break

agentInstanceList = []
killAgent.serverclosed = True 
time.sleep(1)
kill_lock.acquire()
kill_condition.notifyAll()
kill_lock.release()

print "\n-#Printing the Connections#-"
for i in agentList:
	print i 
	if i["Connection"] != None:
		i["Connection"].close()