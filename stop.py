import time as systime

server_list=['wls_ms1','wls_ms2']

def stop(server):
	try:
		connect(sys.argv[1],sys.argv[2],sys.argv[3])
		print server
		cd('domainRuntime:/ServerLifeCycleRuntimes')
		cd(server)
		state=cmo.state
		if(state=='RUNNING'):
			cmo.forceShutdown()
		if(state=='SHUTDOWN'):
			print " Server "+ server + " is already shutdown "
		if(state=='ADMIN'):
			print " Server "+ server + " is in ADMIN STATE Please Check logs and Shutdown manually"
	except:	
		traceback.print_exc(limit=1)

def status(server):
	try:
		print " Checking status of " + server
		connect(sys.argv[1],sys.argv[2],sys.argv[3])
		print server
		cd('domainRuntime:/ServerLifeCycleRuntimes')
		cd(server)
		state=cmo.state
		if(state=='RUNNING'):
			print " Server is still Running"
			stop(server)
		if(state=='SHUTDOWN'):
			print " Server "+ server + " is already shutdown "
			return True
		if(state=='ADMIN'):
			print " Server "+ server + " is in ADMIN STATE Please Check logs and Shutdown manually"
	except:	
		traceback.print_exc(limit=1)
		
		
for i in server_list:
	stop(i)

count=0	
for i in server_list:
	while True:
		if count<5:
			if status(i):
				break
				count=0
			systime.sleep(20)
		if count==5:
			break
			count=0
		count=count+1
	