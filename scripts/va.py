import os
import subprocess
import csv
import numpy

defaultVelocity = 0.03;
duration=2000;

dirName='../output/duration={duration}'.format(duration=duration)

rc=1
data_values_1 = [40, 3.1, 3]
data_values_2 = [100, 5, 4]
data_values_3 = [400, 10, 9]
simulation_data_values = [data_values_1, data_values_2, data_values_3]

data_values_1_p_2 = [50, 5, 4]
data_values_2_p_2 = [100, 7.07, 7]
data_values_3_p_2 = [500, 15.81, 15]
simulation_data_values_p_2 = [data_values_1_p_2, data_values_2_p_2, data_values_3_p_2]

etha_values=[x * 0.5 for x in range(0, 11)]

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory " , dirName ,  " Created ")

for i in range(0, len(simulation_data_values_p_2)):
	N=simulation_data_values_p_2[i][0]
	L=simulation_data_values_p_2[i][1]
	M=simulation_data_values_p_2[i][2]

	with open('{dirName}/N={n}-L={boxSide}-M={matrix}.txt'.format(
		dirName=dirName,
		n=N,
		boxSide=L,
		matrix=M), 'w') as f:
		for e in etha_values:
			averages = [e]
			std = ['std']
			values = []
			for k in range(1, 4):
				os.system('python3 ./generate_for_va.py {N} {L}'.format(N = N, L = L))
				command = 'java -jar ../target/tp2-1.0-SNAPSHOT.jar --dynamicFile=Dynamic-N={n}.txt --radius={rc} --matrix={matrix} --noise={noise} --speed={defaultVelocity} --time={time} --boxSide={L}'.format(
						n=N,
						rc= rc,
						matrix=M,
						noise=e,
						time=duration,
						L=L,
						defaultVelocity=defaultVelocity
						)
				print(command)
				p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
				number = None;
				p.stdout.readline();
				line = p.stdout.readlines()
				number = line[0].decode()
				number = number.replace('\n', '')
				values.append(float(number))
			retval = p.wait()
			averages.append(numpy.mean(values))
			std.append(round(numpy.std(values),5))
			f.write(' '.join([str(x) for x in averages]))
			f.write('\n')
			f.write(' '.join([str(x) for x in std]))
			f.write('\n')