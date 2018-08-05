FROM solomonfield/spark-on-hive:2.3.1-centos
LABEL Solomonfield <whg19961229@gmail.com>

ENV PYTHON_VERSION=3.6.6

# Install Python from source 
RUN yum install -y gcc g++ zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel \
	readline-devel tk-devel gdbm-devel db4-devel libpcap-devel && \
	wget https://www.python.org/ftp/python/${PYTHON_VERSION}/Python-${PYTHON_VERSION}.tgz && \
	tar xvf Python-${PYTHON_VERSION}.tgz && \
	rm -rf Python-${PYTHON_VERSION}.tgz

WORKDIR /root/Python-${PYTHON_VERSION}

RUN ./configure --prefix=/usr/local --enable-optimizations --enable-shared && \
	make -j4 && make install && echo "/usr/local/lib/" >> /etc/ld.so.conf.d/python3.conf && \
	ldconfig && alias python=/usr/local/bin/python3

WORKDIR /root/

RUN pip3 install --upgrade pip && pip install jupyter numpy scipy pandas sklearn && rm -rf Python-${PYTHON_VERSION} && \
	yum remove -y gcc g++ zlib-devel bzip2-devel openssl-devel ncurses-devel \
	sqlite-devel readline-devel tk-devel gdbm-devel db4-devel libpcap-devel wget && \
	yum clean all && \
    rm -rf /var/cache/yum

ENTRYPOINT [ "sh", "-c", "/root/spark-entrypoint.sh; bash"]
