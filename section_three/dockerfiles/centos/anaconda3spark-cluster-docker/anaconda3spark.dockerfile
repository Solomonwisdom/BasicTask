FROM solomonfield/spark-on-hive:2.3.1-centos
LABEL Solomonfield <whg19961229@gmail.com>

# Install Python from Anaconda 
RUN wget https://mirrors.tuna.tsinghua.edu.cn/anaconda/archive/Anaconda3-5.2.0-Linux-x86_64.sh -O ~/anaconda.sh && \
    /bin/bash ~/anaconda.sh -b -p /opt/conda && \
	rm ~/anaconda.sh && \
    /opt/conda/bin/conda update --all -y

ENV PATH /opt/conda/bin/:/opt/conda/sbin/:$PATH

ENTRYPOINT [ "sh", "-c", "/root/spark-entrypoint.sh; bash"]
