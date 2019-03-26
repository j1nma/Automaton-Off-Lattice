import os
import subprocess
import csv
import numpy

defaultVelocity = 0.03;
duration=1000;

dirName='output/duration={duration}'.format(duration=duration)

rc=1
data_values_1 = [40, 3.1, 's', 3]
data_values_2 = [100, 5, '+', 4]
data_values_3 = [400, 10, 'x', 9]

# rc=0.5
# data_values_1 = [40, 3.1, 's', 6]
# data_values_2 = [100, 5, '+', 8]
# data_values_3 = [400, 10, 'x', 18]

simulation_data_values = [data_values_1, data_values_2, data_values_3]

etha_values=[x * 0.25 for x in range(0, 21)];

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory " , dirName ,  " Created ")

for i in range(0, len(simulation_data_values)):
	N=simulation_data_values[i][0]
	L=simulation_data_values[i][1]
	M=simulation_data_values[i][3]
	marker=simulation_data_values[i][2]

	with open('{dirName}/N={n}-L={boxSide}-M={matrix}.txt'.format(
		dirName=dirName,
		n=N,
		boxSide=L,
		matrix=M), 'w') as f:
		for e in etha_values:
			averages = [e]
			std = ['std']
			values = []
			for k in range(1, 6):
				command = 'java -jar ./target/tp2-1.0-SNAPSHOT.jar --dynamicFile=random/Dynamic-N={n}.txt --radius={rc} --matrix={matrix} --noise={noise} --speed={defaultVelocity} --time={time} --boxSide={L}'.format(
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