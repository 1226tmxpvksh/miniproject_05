<template>
    <v-data-table
        :headers="headers"
        :items="items"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { VDataTable } from 'vuetify/labs/VDataTable'

export default {
    name: 'BookDetailInfoView',
    components: {
        VDataTable,
    },
    props: {
        value: Object,
        editMode: Boolean,
        isNew: Boolean
    },
    setup() {
        const headers = ref([
            // 필드 디스크립터를 기반으로 헤더 설정
            { title: "bookId", key: "bookId" },
            { title: "title", key: "title" },
            { title: "content", key: "content" },
            { title: "viewCount", key: "viewCount" },
            { title: "writerId", key: "writerId" },
            { title: "writerNickname", key: "writerNickname" },
            { title: "coverUrl", key: "coverUrl" },
        ]);

        const items = ref([]);

        onMounted(async () => {
            try {
                const response = await axios.get('/bookDetailInfos');
                const data = response.data._embedded.bookDetailInfos;
                data.forEach(obj => {
                    obj.id = obj._links.self.href.split("/").pop();
                });
                items.value = data;
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        });

        return {
            headers,
            items
        };
    }
}
</script>
