import os
import subprocess
import csv
import numpy

dirName="output"

duration=1000;

simOutputFile = "./output.txt";
rc=0.5

# data from Paper
# N, L, grap symbol, M
#simulation_data_values={40, 3.1, 's', 3; 100, 5, '+',4; 400, 10, 'x', 8; 4000, 31.6, '^', 30; 10000, 50, 'd', 40}

data_values_1 = [40, 3.1, 's', 6]
data_values_2 = [100, 5, '+',8]
data_values_3 = [400, 10, 'x', 18]

simulation_data_values = [data_values_1, data_values_2, data_values_3]

etha_values=[x * 0.25 for x in range(0, 21)];

times=20;

if not os.path.exists(dirName):
        os.mkdir(dirName)
        print("Directory " , dirName ,  " Created ")

# with open('output/va.csv', 'w') as f:
# 	csv_writer = csv.writer(f, delimiter=';', quotechar='|', quoting=csv.QUOTE_MINIMAL)
# 	header = ['Matrix {n}x{n}'.format(n=i) for i in range(1, 14)]
# 	header.insert(0, 'file')
# 	header.append('Brute Force')
# 	csv_writer.writerow(header)
# 	for x in range(1, 2):
# 		averages = [x]
# 		std = ['std']
# 		for y in range(1, 15):
# 			values = []
# 			for z in range(1, 5):
# 				command = 'java -jar ./target/tp2-1.0-SNAPSHOT.jar --dynamicFile=random/Dynamic-N={n}.txt --radius={rc} --matrix={matrix} --noise={noise} --speed=0.03 --time={time} --boxSide={L}'.format(
# 					n=numberOfParticles,
# 					rc= rc,
# 					matrix=y,
# 					noise=noise,
# 					time=time,
# 					L=boxSide
# 					)
# 				print(command)
# 				p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
# 				number = None;
# 				for line in p.stdout.readlines():
# 					print(line)
# 					number = line.decode().split(' ')
# 					number = number[-1]
# 					number = number.replace('ms\n', '')
# 					break;
# 				values.append(int(number))
# 			retval = p.wait()
# 			averages.append(numpy.mean(values))
# 			std.append(round(numpy.std(values),2))
# 		csv_writer.writerow(averages)
# 		csv_writer.writerow(std)