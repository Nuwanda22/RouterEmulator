#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <wiringPi.h>

#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include "json.hpp"

#define LED 1

class ClientSocket {
private:
	int new_socket;
public:
	ClientSocket(int client) {
		new_socket = client;
	}

	void receive(char *buffer, int length) {
		recv(new_socket, buffer, length, 0);
	}

	void send(char *message, int length) {
		::send(new_socket, message, length, 0);
	}

	void close() {
		::close(new_socket);
	}
};

class ServerSocket {
private:
	int server_fd;
	int new_socket;
	struct sockaddr_in address;
public:
	ServerSocket(int port) {
		if((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
			perror("socket failed");
			exit(EXIT_FAILURE);
		}
		
		int opt = 1;
		// set socket option
		if(setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt, sizeof(opt))) {
			perror("setsockopt");
			exit(EXIT_FAILURE);
		}

		address.sin_family = AF_INET;
		address.sin_addr.s_addr = INADDR_ANY;
		address.sin_port = htons(port);
	}

	void bind() {
		if (::bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
			perror("bind failed");
			exit(EXIT_FAILURE);
		}
	}

	void listen() {
		if (::listen(server_fd, 3) < 0) {
			perror("listen");
			exit(EXIT_FAILURE);
		}
	}

	ClientSocket accept() {
		int new_socket, addrlen = sizeof(address);
		if ((new_socket = ::accept(server_fd, (struct sockaddr *)&address, (socklen_t *)&addrlen)) < 0) {
			perror("accept");
			exit(EXIT_FAILURE);
		}
		
		return ClientSocket(new_socket);		
	}

	void close() {
		::close(server_fd);
	}
};

using json = nlohmann::json;

int main(int argc, char **argv) 
{
	wiringPiSetup();

	pinMode(LED, OUTPUT);

	ServerSocket socket(5000); 	printf("[Log] Create socket\n");
	socket.bind();			printf("[Log] Bind\n");
	socket.listen();		printf("[Log] Listen\n");

	ClientSocket client = socket.accept();		printf("[Log] Accept\n");
	char buffer[1024] = {0};

	while(1) {
try{
		memset(buffer, 0, sizeof(buffer));
		client.receive(buffer, sizeof(buffer));	printf("[Log] Receive\n");
		printf("[Info] Request: %s\n", buffer);

		auto request = json::parse(buffer);
		auto command = request["command"];
		std::string response;

		if(command == "power") {
			bool value = request["value"];
			if(value) {
				//turn on
				digitalWrite(LED, HIGH);
				printf("[Log] Turn on\n");
			} else {
				// turn off
				digitalWrite(LED, LOW);
				printf("[Log] Turn off\n");
			}

			json res = {
				{"command", command},
				{"status", true},
				{"result", {{"value", value}}}
			};
			response = res.dump();
		} else {
			json res = {
				{"command", command},
				{"status", false},
				{"message", "Not implemented."}
			};
			response = res.dump();
		}
		
		strcpy(buffer, response.c_str());
		client.send(buffer, sizeof(buffer));		printf("[Log] Send\n");
		printf("[Info] Response: %s\n", buffer);
} catch(std::invalid_argument) {
	client.close();
	std::cout << "[Log] Close client" << std::endl;
	break;
}
	}

	socket.close();
	std::cout << "[Log] Close server" << std::endl;
	
	return 0;
}
