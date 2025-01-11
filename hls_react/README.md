docker build -t my-react-nginx-rtmp .

docker run -d -p 1935:1935 -p 8080:8080 --name my-react-nginx-rtmp-container my-react-nginx-rtmp
