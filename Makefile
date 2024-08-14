test: #A build of the app
	make -C app test

run-dist: #Launch of the program

build: #A build of the app
	make -C app build

clean:
	make -C app clean

lint: #Chech a style of code via Checkstyle
	make -C app lint

report: #Make a JaCoCo Report
	make -C app report

.PHONY: build
